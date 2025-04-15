package org.example.springbootcrudrest.repository.inMem;

import lombok.NonNull;
import org.example.springbootcrudrest.model.Customer;
import org.example.springbootcrudrest.repository.db.CustomerRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository

public class InMemoryCustomerRepositoryImpl implements InMemoryCustomerRepository {

    // properties
    private final List<Customer> customers;

    // injection
    private final CustomerRepository customerRepository;

    public InMemoryCustomerRepositoryImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
        this.customers = customerRepository.findAll();
    }

    @Override
    public Optional<Customer> findById(@NonNull Long id) {
        return customers
                .stream()
                .filter(customer -> customer.getId().equals(id))
                .findFirst();
    }

    @Override
    public Optional<Customer> findByUsername(@NonNull String username) {
        return customers
                .stream()
                .filter(customer -> customer.getUsername().equalsIgnoreCase(username))
                .findFirst();
    }

    @Override
    public List<Customer> findAll() {
        return new ArrayList<>(customers);
    }

    @Override
    public Customer save(@NonNull Customer customer) {
        customers.stream()
                .filter(c -> c.getUsername().equalsIgnoreCase(customer.getUsername()))
                .findFirst()
                .ifPresentOrElse(c -> {}, () -> customers.add(customer));
        return customer;
    }

    @Override
    public void delete(@NonNull Customer customer) {
        customers.removeIf(c -> c.getUsername().equalsIgnoreCase(customer.getUsername()));
    }

    @Override
    public void update(@NonNull Customer customer) {
        customers.stream()
                .filter(c -> c.getUsername().equalsIgnoreCase(customer.getUsername()))
                .findFirst()
                .ifPresent(c -> {
                    partialUpdate(c, customer);
                });
    }

    @Override
    public void refresh() {
        customers.clear();
        customers.addAll(customerRepository.findAll());
    }

    private void partialUpdate(Customer oldCustomer, Customer newCustomer) {
        oldCustomer.setUsername(newCustomer.getUsername());
        oldCustomer.setPassword(newCustomer.getPassword());
        oldCustomer.setFirstname(newCustomer.getFirstname());
        oldCustomer.setLastname(newCustomer.getLastname());
    }
}
