package org.example.springbootcrudrest.unit.repo;

import org.example.springbootcrudrest.exception.DuplicateEntityException;
import org.example.springbootcrudrest.exception.NotFoundException;
import org.example.springbootcrudrest.model.Customer;
import org.example.springbootcrudrest.repository.cachesync.CustomerCacheSynchronizerImpl;
import org.example.springbootcrudrest.repository.db.CustomerRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
@DisplayName("Cache Synchronizer Tests")
public class CacheSynchronizerTest {

    @Mock
    private CustomerRepository customerRepository;


    @InjectMocks
    private CustomerCacheSynchronizerImpl cacheSynchronizer;

    private List<Customer> customers;
    private Customer customer;

    @BeforeEach
    void setUp() {
        customers = new ArrayList<>();
        try {
            Field field = CustomerCacheSynchronizerImpl.class.getDeclaredField("customers");
            field.setAccessible(true);
            field.set(cacheSynchronizer, customers);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        customer = new Customer();
        customer.setId(1L);
        customer.setUsername("testuser");
        customer.setFirstname("Test");
        customer.setLastname("User");
        customer.setPassword("password");
        customer.setActive(true);
    }

    @Test
    void initialize() {
        Mockito.when(customerRepository.findAll()).thenReturn(List.of(customer));

        cacheSynchronizer.initialize();

        Assertions.assertEquals(1, customers.size(), "Cache must contain one customer");
        Assertions.assertEquals("testuser", customers.get(0).getUsername(), "Username should match");
        Mockito.verify(customerRepository, Mockito.times(1)).findAll();
    }

    @Test
    void clear() {
        cacheSynchronizer.add(customer);

        cacheSynchronizer.clear();

        Assertions.assertTrue(cacheSynchronizer.getAll().isEmpty(), "cache should be empty");
    }

    @Test
    void refresh() {

        Mockito.when(customerRepository.findAll()).thenReturn(List.of(customer));
        cacheSynchronizer.add(new Customer());

        cacheSynchronizer.refresh();

        Assertions.assertEquals(1, customers.size(), "Cache must contain one customer");
        Assertions.assertEquals("testuser", customers.get(0).getUsername(), "Username should match");
        Mockito.verify(customerRepository, Mockito.times(1)).findAll();
    }

    @Test
    void add() {

        cacheSynchronizer.add(customer);

        Assertions.assertEquals(1, customers.size(), "list size mismatch");
        Assertions.assertEquals("testuser", customers.get(0).getUsername(), "Username should match");
    }

    @Test
    void throwDuplicateEntityException() {

        cacheSynchronizer.add(customer);

        Assertions.assertThrows(DuplicateEntityException.class, () -> cacheSynchronizer.add(customer));
    }

    @Test
    void update() {
        cacheSynchronizer.add(customer);

        customer.setUsername("updateduser");
        customer.setFirstname("updatedfirstname");
        customer.setLastname("updatedlastname");
        customer.setPassword("updatedpassword");
        customer.setActive(false);

        cacheSynchronizer.update(customer);
        Assertions.assertEquals(1, customers.size(), "Cache must contain one customer");
        Assertions.assertEquals("updateduser", customers.get(0).getUsername(), "Username should match");
        Assertions.assertEquals("updatedfirstname", customers.get(0).getFirstname(), "Firstname should match");
        Assertions.assertEquals("updatedlastname", customers.get(0).getLastname(), "Lastname should match");
        Assertions.assertEquals("updatedpassword", customers.get(0).getPassword(), "Password should match");
        Assertions.assertFalse(customers.get(0).getActive(), "Active should match");
    }

    @Test
    void throwNotFoundEntityException() {
        Assertions.assertThrows(NotFoundException.class, () -> cacheSynchronizer.update(customer));
    }

    @Test
    void delete() {
        cacheSynchronizer.add(customer);

        cacheSynchronizer.remove(customer);

        Assertions.assertEquals(0, customers.size(), "Cache must contain one customer");
    }

    @Test
    void getAll() {
        cacheSynchronizer.add(customer);

        List<Customer> allCustomers = cacheSynchronizer.getAll();

        Assertions.assertNotNull(allCustomers, "customer list should not be null");
        Assertions.assertEquals(allCustomers.size(), customers.size(), "Cache must contain one customer");

    }
}
