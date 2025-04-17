package org.example.springbootcrudrest.unit.service;

import org.example.springbootcrudrest.model.Customer;
import org.example.springbootcrudrest.repository.inMem.InMemoryCustomerRepository;
import org.example.springbootcrudrest.service.InMemoryCustomerServiceImpl;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName(DisplayNameGenerator.DEFAULT_GENERATOR_PROPERTY_NAME)
public class InMemoryCustomerServiceTest {

    @Mock
    private InMemoryCustomerRepository customerRepository;

    @InjectMocks
    private InMemoryCustomerServiceImpl customerService;

    private Customer customer;

    @BeforeEach
    void setUp() {
        customer = new Customer();
        customer.setId(1L);
        customer.setUsername("testuser");
        customer.setFirstname("test");
        customer.setLastname("test");
        customer.setPassword("password");
        customer.setActive(true);
    }

    @Test
    void createCustomer() {
        Assumptions.assumeTrue(customer != null);
        Mockito.when(customerRepository.save(customer)).thenReturn(customer);

        Customer savedCustomer = customerService.createCustomer(customer);

        Assertions.assertNotNull(savedCustomer, "customer cannot be null");
        Assertions.assertEquals(customer.getId(), savedCustomer.getId());
        Assertions.assertEquals(customer.getUsername(), savedCustomer.getUsername());
        Assertions.assertEquals(customer.getFirstname(), savedCustomer.getFirstname());
        Assertions.assertEquals(customer.getLastname(), savedCustomer.getLastname());
        Assertions.assertEquals(customer.getPassword(), savedCustomer.getPassword());
        Assertions.assertEquals(customer.getActive(), savedCustomer.getActive());
        Mockito.verify(customerRepository, Mockito.times(1)).save(customer);
    }

    @Test
    void updateCustomer() {
        String name = "testuser";
        Assumptions.assumeTrue(customer != null);
        Mockito.doNothing().when(customerRepository).update(customer);

        customerService.updateCustomer(name, customer);

        Assertions.assertNotNull(customer.getId());
        Assertions.assertNotNull(customer.getUsername());
        Assertions.assertNotNull(customer.getPassword());
        Assertions.assertNotNull(customer.getActive());
        Mockito.verify(customerRepository).update(customer);
    }

    @Test
    void deleteCustomer() {
        Assumptions.assumeTrue(customer != null);
        Mockito.doNothing().when(customerRepository).delete(customer);

        customerService.deleteCustomer(customer);

        Mockito.verify(customerRepository, Mockito.times(1)).delete(customer);
    }
}
