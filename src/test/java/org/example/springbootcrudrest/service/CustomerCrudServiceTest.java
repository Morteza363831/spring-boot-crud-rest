package org.example.springbootcrudrest.service;

import org.example.springbootcrudrest.exception.DuplicateEntityException;
import org.example.springbootcrudrest.exception.NotFoundException;
import org.example.springbootcrudrest.model.*;
import org.example.springbootcrudrest.repository.db.CustomerRepository;
import org.example.springbootcrudrest.repository.inMem.InMemoryCustomerRepository;
import org.example.springbootcrudrest.utility.Validator;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@DisplayName(DisplayNameGenerator.DEFAULT_GENERATOR_PROPERTY_NAME)
public class CustomerCrudServiceTest {

    @Mock
    private InMemoryCustomerRepository inMemoryCustomerRepository;

    @Mock
    private DatabaseCustomerServiceImpl databaseCustomerService;

    @Mock
    private InMemoryCustomerServiceImpl inMemoryCustomerService;

    @Mock
    private CustomerMapper customerMapper;

    @Mock
    private Validator validator;

    @InjectMocks
    private CustomerCrudServiceImpl customerCrudService;

    private Customer customer;
    private CustomerCreateDto createDto;
    private CustomerResultDto resultDto;

    @BeforeEach
    void setUp() {
        // Initialize test data
        customer = new Customer();
        customer.setId(1L);
        customer.setUsername("testuser");
        customer.setFirstname("Test");
        customer.setLastname("User");
        customer.setPassword("password");
        customer.setActive(true);

        createDto = new CustomerCreateDto()
                .setUsername("testuser")
                .setFirstname("Test")
                .setLastname("User")
                .setPassword("password");

        resultDto = new CustomerResultDto(1L, "testuser", "Test", "User");
    }

    @Test
    @DisplayName(DisplayNameGenerator.Simple.DEFAULT_GENERATOR_PROPERTY_NAME)
    void shouldCreateCustomerSuccessfully() {
        // Arrange
        Mockito.when(inMemoryCustomerRepository.findByUsername("testuser")).thenReturn(Optional.empty());
        Mockito.when(customerMapper.toEntity(createDto)).thenReturn(customer);
        Mockito.when(databaseCustomerService.createCustomer(customer)).thenReturn(customer);
        Mockito.when(inMemoryCustomerService.createCustomer(customer)).thenReturn(customer);
        Mockito.when(customerMapper.toDto(customer)).thenReturn(resultDto);

        // Act
        CustomerResultDto result = customerCrudService.createCustomer(createDto);

        // Assert
        Assertions.assertNotNull(result, "result DTO should not be null");
        Assertions.assertEquals("testuser", result.username(), "Username should match");
        Mockito.verify(validator).validate(createDto);
        Mockito.verify(databaseCustomerService).createCustomer(customer);
        Mockito.verify(inMemoryCustomerService).createCustomer(customer);
        Mockito.verify(customerMapper).toDto(customer);
    }

    @Test
    void shouldThrowDuplicateEntityException() {

        Mockito.when(inMemoryCustomerRepository.findByUsername("testuser")).thenReturn(Optional.of(customer));

        DuplicateEntityException duplicateEntityException = Assertions.assertThrows(DuplicateEntityException.class, () -> customerCrudService.createCustomer(createDto));

        Assertions.assertEquals("testuser", duplicateEntityException.getMessage());
        Mockito.verify(validator).validate(createDto);
        Mockito.verify(inMemoryCustomerRepository).findByUsername("testuser");
        Mockito.verifyNoInteractions(databaseCustomerService, inMemoryCustomerService);
    }

    @Test
    void shouldGetCustomerByIdSuccessfully() {
        Assumptions.assumeTrue(customer.getId() != null, "Customer id should not be null for this test");
        Mockito.when(inMemoryCustomerRepository.findById(1L)).thenReturn(Optional.of(customer));
        Mockito.when(customerMapper.toDto(customer)).thenReturn(resultDto);

        CustomerResultDto result = customerCrudService.getCustomerById(1L);

        Assertions.assertNotNull(result, "result DTO should not be null");
        Assertions.assertEquals("testuser", result.username(), "Username should match");
        Mockito.verify(inMemoryCustomerRepository).findById(1L);
        Mockito.verify(customerMapper).toDto(customer);
    }

    @Test
    void shouldThrowEntityNotFoundExceptionForNotExistentId() {
        Mockito.when(inMemoryCustomerRepository.findById(1L)).thenReturn(Optional.empty());

        NotFoundException notFoundException = Assertions.assertThrows(NotFoundException.class, () -> customerCrudService.getCustomerById(1L));

        Assertions.assertEquals("1", notFoundException.getMessage());
        Mockito.verify(inMemoryCustomerRepository).findById(1L);
        Mockito.verifyNoInteractions(customerMapper);
    }

    @Test
    void shouldUpdateCustomerSuccessfully() {
        CustomerUpdateDto updateDto = new CustomerUpdateDto()
                .setUsername("testuser")
                .setFirstname("Test")
                .setLastname("User")
                .setPassword("password");

        Mockito.when(inMemoryCustomerRepository.findByUsername("testuser")).thenReturn(Optional.of(customer));
        Mockito.doNothing().when(validator).validate(updateDto);
        Mockito.when(customerMapper.partialUpdate(updateDto, customer)).thenReturn(customer);
        Mockito.doNothing().when(databaseCustomerService).updateCustomer("testuser", customer);
        Mockito.doNothing().when(inMemoryCustomerService).updateCustomer("testuser", customer);

        customerCrudService.updateCustomer("testuser", updateDto);

        Mockito.verify(validator).validate(updateDto);
        Mockito.verify(inMemoryCustomerRepository).findByUsername("testuser");
        Mockito.verify(customerMapper).partialUpdate(updateDto, customer);
        Mockito.verify(databaseCustomerService).updateCustomer("testuser", customer);
        Mockito.verify(inMemoryCustomerService).updateCustomer("testuser", customer);
    }

    @Test
    void shouldThrowNotFoundExceptionForNotExistentUpdate() {
        CustomerUpdateDto updateDto = new CustomerUpdateDto()
                .setUsername("testuser")
                .setFirstname("Updated")
                .setLastname("User")
                .setPassword("newpassword");

        Mockito.when(inMemoryCustomerRepository.findByUsername("testuser")).thenReturn(Optional.empty());
        Mockito.doNothing().when(validator).validate(updateDto);

        NotFoundException notFoundException = Assertions.assertThrows(NotFoundException.class, () -> customerCrudService.updateCustomer("testuser", updateDto),
                "Should throw NotFoundException for not-existent update");

        Assertions.assertEquals("testuser", notFoundException.getMessage());
        Mockito.verify(validator).validate(updateDto);
        Mockito.verify(inMemoryCustomerRepository).findByUsername("testuser");
        Mockito.verifyNoInteractions(customerMapper, databaseCustomerService, inMemoryCustomerService);
    }

    @Test
    void shouldGetAllCustomersSuccessfully() {
        List<Customer> customers = Collections.singletonList(customer);
        Mockito.when(inMemoryCustomerRepository.findAll()).thenReturn(customers);
        Mockito.when(customerMapper.toDto(customer)).thenReturn(resultDto);

        List<CustomerResultDto> result = customerCrudService.getAllCustomers();

        Assertions.assertNotNull(result, "result DTO should not be null");
        Assertions.assertEquals("testuser", result.get(0).username(), "Username should match");
        Mockito.verify(inMemoryCustomerRepository).findAll();
        Mockito.verify(customerMapper).toDto(customer);
    }

    @Test
    void shouldRemoveCustomerSuccessfully() {
        CustomerDeleteDto deleteDto = new CustomerDeleteDto()
                .setUsername("testuser");

        Mockito.when(inMemoryCustomerRepository.findByUsername("testuser")).thenReturn(Optional.of(customer));
        Mockito.doNothing().when(validator).validate(deleteDto);

        customerCrudService.deleteCustomer(deleteDto);

        Mockito.verify(validator).validate(deleteDto);
        Mockito.verify(inMemoryCustomerRepository).findByUsername("testuser");
    }

}
