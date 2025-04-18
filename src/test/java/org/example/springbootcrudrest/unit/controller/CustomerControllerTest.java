package org.example.springbootcrudrest.unit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.example.springbootcrudrest.controller.CustomerController;
import org.example.springbootcrudrest.exception.CustomValidationException;
import org.example.springbootcrudrest.exception.DuplicateEntityException;
import org.example.springbootcrudrest.exception.NotFoundException;
import org.example.springbootcrudrest.model.CustomerCreateDto;
import org.example.springbootcrudrest.model.CustomerDeleteDto;
import org.example.springbootcrudrest.model.CustomerResultDto;
import org.example.springbootcrudrest.model.CustomerUpdateDto;
import org.example.springbootcrudrest.service.CustomerCrudService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.Set;


@Slf4j
@WebMvcTest(controllers = CustomerController.class)
@DisplayName("Customer Controller Tests")
public class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;

    @MockitoBean
    private CustomerCrudService customerCrudService;
    private CustomerResultDto resultDto;
    private CustomerCreateDto createDto;

    @BeforeEach
    void setUp() {
        createDto = new CustomerCreateDto();
        createDto.setUsername("testuser");
        createDto.setFirstname("Test");
        createDto.setLastname("User");
        createDto.setPassword("password");
        resultDto = new CustomerResultDto(1L, "testuser", "Test", "User");
    }

    @Test
    void getAllCustomers() throws Exception {
        Mockito.when(customerCrudService.getAllCustomers()).thenReturn(List.of(resultDto));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/customers"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value( "success"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.size()").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].username").value("testuser"));

        Mockito.verify(customerCrudService, Mockito.times(1)).getAllCustomers();
    }

    @Test
    void getCustomerById() throws Exception {
        Mockito.when(customerCrudService.getCustomerById(1L)).thenReturn(resultDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/customers/id/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value( "success"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.username").value("testuser"));
        Mockito.verify(customerCrudService, Mockito.times(1)).getCustomerById(1L);
    }

    @Test
    void throwNotFoundExceptionForGetCustomerById() throws Exception {
        Mockito.when(customerCrudService.getCustomerById(1L)).thenThrow(new NotFoundException("1"));

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/customers/id/1"))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value( "fail"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").exists())
                .andReturn();

        log.error(mvcResult.getResponse().getContentAsString());

        Mockito.verify(customerCrudService, Mockito.times(1)).getCustomerById(1L);
    }

    @Test
    void getCustomerByUsername() throws Exception {
        Mockito.when(customerCrudService.getCustomerByName("testuser")).thenReturn(resultDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/customers/testuser"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value( "success"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.username").value("testuser"));

        Mockito.verify(customerCrudService, Mockito.times(1)).getCustomerByName("testuser");
    }

    @Test
    void throwNotFoundExceptionForGetCustomerByUsername() throws Exception {
        Mockito.when(customerCrudService.getCustomerByName("testuser")).thenThrow(new NotFoundException("testuser"));

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/customers/testuser"))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value( "fail"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").exists())
                .andReturn();

        log.error(mvcResult.getResponse().getContentAsString());

        Mockito.verify(customerCrudService, Mockito.times(1)).getCustomerByName("testuser");
    }

    @Test
    void createCustomer() throws Exception {
        Mockito.when(customerCrudService.createCustomer(ArgumentMatchers.any(CustomerCreateDto.class))).thenReturn(resultDto);

        mockMvc.perform(
                MockMvcRequestBuilders
                        .post("/api/v1/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(createDto))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value( "success"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.username").value("testuser"));

        Mockito.verify(customerCrudService, Mockito.times(1)).createCustomer(ArgumentMatchers.any(CustomerCreateDto.class));
    }

    @Test
    void throwDuplicateEntityException() throws Exception {
        Mockito.when(customerCrudService.createCustomer(ArgumentMatchers.any(CustomerCreateDto.class))).thenThrow(new DuplicateEntityException("testuser"));

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .post("/api/v1/customers")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(createDto))
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isConflict())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value( "fail"));

        Mockito.verify(customerCrudService, Mockito.times(1)).createCustomer(ArgumentMatchers.any(CustomerCreateDto.class));
    }

    @Test
    void throwBadRequestForCreateCustomer() throws Exception {

        Mockito.when(customerCrudService.createCustomer(ArgumentMatchers.any(CustomerCreateDto.class))).thenThrow(new CustomValidationException(Set.of()));

        MvcResult mvcResult = mockMvc.perform(
                        MockMvcRequestBuilders
                                .post("/api/v1/customers")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(createDto))
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value( "fail"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").exists())
                .andReturn();

        log.error(mvcResult.getResponse().getContentAsString());

        Mockito.verify(customerCrudService, Mockito.times(1)).createCustomer(ArgumentMatchers.any(CustomerCreateDto.class));
    }

    @Test
    void updateCustomer() throws Exception {
        CustomerUpdateDto updateDto = new CustomerUpdateDto();
        updateDto.setUsername("updateuser");
        updateDto.setFirstname("Test");
        updateDto.setLastname("User");
        updateDto.setPassword("password");
        Mockito.doNothing().when(customerCrudService).updateCustomer(ArgumentMatchers.any(String.class), ArgumentMatchers.any(CustomerUpdateDto.class));

        mockMvc.perform(
                MockMvcRequestBuilders
                        .put("/api/v1/customers/testuser")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(updateDto))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value( "success"));

        Mockito
                .verify(customerCrudService, Mockito.times(1))
                .updateCustomer(ArgumentMatchers.any(String.class), ArgumentMatchers.any(CustomerUpdateDto.class));
    }

    @Test
    void deleteCustomer() throws Exception {
        CustomerDeleteDto deleteDto = new CustomerDeleteDto();
        deleteDto.setUsername("testuser");

        Mockito.doNothing().when(customerCrudService).deleteCustomer(ArgumentMatchers.any(CustomerDeleteDto.class));

        mockMvc.perform(
                MockMvcRequestBuilders
                        .delete("/api/v1/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(deleteDto))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value( "success"));

        Mockito.verify(customerCrudService, Mockito.times(1)).deleteCustomer(ArgumentMatchers.any(CustomerDeleteDto.class));
    }

}
