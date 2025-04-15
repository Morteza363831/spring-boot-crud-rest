package org.example.springbootcrudrest.repository.inMem;

import org.example.springbootcrudrest.model.Customer;

import java.util.List;
import java.util.Optional;

public interface InMemoryCustomerRepository {

    Optional<Customer> findById(Long id);

    Optional<Customer> findByUsername(String username);

    List<Customer> findAll();

    void save(Customer customer);

    void delete(Customer customer);

    void update(Customer customer);

    void refresh();
}
