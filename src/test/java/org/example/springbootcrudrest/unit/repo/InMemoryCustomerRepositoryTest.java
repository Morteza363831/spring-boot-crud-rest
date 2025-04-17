package org.example.springbootcrudrest.unit.repo;

import org.example.springbootcrudrest.model.Customer;
import org.example.springbootcrudrest.repository.cachesync.CustomerCacheSynchronizerImpl;
import org.example.springbootcrudrest.repository.inMem.InMemoryCustomerRepositoryImpl;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@DisplayName("In Memory Customer Repository Tests")
public class InMemoryCustomerRepositoryTest {

    @Mock
    private CustomerCacheSynchronizerImpl cacheSynchronizer;

    @InjectMocks
    private InMemoryCustomerRepositoryImpl customerRepository;

    private List<Customer> customers;
    private Customer customer;

    @BeforeEach
    void setUp() {

        customers = new ArrayList<>();

        customer = new Customer();
        customer.setId(1L);
        customer.setUsername("testuser");
        customer.setFirstname("Test");
        customer.setLastname("User");
        customer.setPassword("password");
        customer.setActive(true);

        customers.add(customer);
    }

    @Test
    void init() {
        Mockito.when(cacheSynchronizer.getAll()).thenReturn(customers);

        List<Customer> allCustomers = customerRepository.findAll();

        Assertions.assertNotNull(allCustomers, "return list cannot be null");
        Assertions.assertEquals(customers.size(), allCustomers.size(), "list size mismatch");
        Mockito.verify(cacheSynchronizer, Mockito.times(1)).getAll();
    }

    @Test
    void findById() {
        Long id = 1L;
        Mockito.when(cacheSynchronizer.getAll()).thenReturn(customers);

        Optional<Customer> foundCustomer = customerRepository.findById(id);

        Assertions.assertTrue(foundCustomer.isPresent(), "customer not found");
        Assertions.assertEquals(id, foundCustomer.get().getId(), "id mismatch");
        Mockito.verify(cacheSynchronizer, Mockito.times(1)).getAll();
    }

    @Test
    void findByUsername() {
        String username = "testuser";
        Mockito.when(cacheSynchronizer.getAll()).thenReturn(customers);

        Optional<Customer> foundCustomer = customerRepository.findByUsername(username);

        Assertions.assertTrue(foundCustomer.isPresent(), "customer not found");
        Assertions.assertEquals(username, foundCustomer.get().getUsername(), "username mismatch");
        Mockito.verify(cacheSynchronizer, Mockito.times(1)).getAll();
    }

    @Test
    void findAll() {
        Mockito.when(cacheSynchronizer.getAll()).thenReturn(customers);

        List<Customer> allCustomers = customerRepository.findAll();

        Assertions.assertNotNull(allCustomers, "return list cannot be null");
        Assertions.assertEquals(customers.size(), allCustomers.size(), "list size mismatch");
        Mockito.verify(cacheSynchronizer, Mockito.times(1)).getAll();
    }

    @Test
    void save() {
        Assumptions.assumeTrue(customer != null, "customer is null");
        Mockito.doNothing().when(cacheSynchronizer).add(customer);

        customerRepository.save(customer);

        Mockito.verify(cacheSynchronizer, Mockito.times(1)).add(customer);
    }

    @Test
    void delete() {
        Assumptions.assumeTrue(customer != null, "customer is null");
        Mockito.doNothing().when(cacheSynchronizer).remove(customer);

        customerRepository.delete(customer);

        Mockito.verify(cacheSynchronizer, Mockito.times(1)).remove(customer);
    }

    @Test
    void update() {
        Assumptions.assumeTrue(customer != null, "customer is null");
        Mockito.doNothing().when(cacheSynchronizer).update(customer);

        customerRepository.update(customer);

        Mockito.verify(cacheSynchronizer, Mockito.times(1)).update(customer);
    }

    @Test
    void refresh() {
        Mockito.doNothing().when(cacheSynchronizer).refresh();

        customerRepository.refresh();

        Mockito.verify(cacheSynchronizer, Mockito.times(1)).refresh();
    }
}
