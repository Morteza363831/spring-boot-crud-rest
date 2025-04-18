package org.example.springbootcrudrest.integration.repo;

import org.example.springbootcrudrest.model.Customer;
import org.example.springbootcrudrest.repository.db.CustomerRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Optional;

@DataJpaTest
@DisplayName("Database Repository Tests")
public class DatabaseRepositoryTest {

    @Autowired
    private CustomerRepository customerRepository;

    private Customer firstCustomer;
    private Customer secondCustomer;

    @BeforeEach
    void setUp() {

        firstCustomer = new Customer();
        firstCustomer.setUsername("testuser");
        firstCustomer.setFirstname("Test");
        firstCustomer.setLastname("User");
        firstCustomer.setPassword("password");
        firstCustomer.setActive(true);

        secondCustomer = new Customer();
        secondCustomer.setUsername("testuser");
        secondCustomer.setFirstname("Test2");
        secondCustomer.setLastname("User2");
        secondCustomer.setPassword("password2");
        secondCustomer.setActive(true);
    }

    @Test
    void findByUsername() {

        customerRepository.save(firstCustomer);

        Optional<Customer> customer = customerRepository.findByUsername("testuser");

        Assertions.assertTrue(customer.isPresent());
        Assertions.assertEquals(customer.get().getUsername(), "testuser");
    }

    @Test
    void throwDataIntegrityViolationException() {
        customerRepository.save(firstCustomer);

        Assertions.assertThrows(DataIntegrityViolationException.class, () -> customerRepository.save(secondCustomer));
    }

}
