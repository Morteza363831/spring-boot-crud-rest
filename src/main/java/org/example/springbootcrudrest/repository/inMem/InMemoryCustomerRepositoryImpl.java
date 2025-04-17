package org.example.springbootcrudrest.repository.inMem;

import jakarta.annotation.PostConstruct;
import lombok.NonNull;
import org.example.springbootcrudrest.model.Customer;
import org.example.springbootcrudrest.repository.cachesync.CacheSynchronizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Repository
public class InMemoryCustomerRepositoryImpl implements InMemoryCustomerRepository {

    @Autowired
    @Qualifier(value = "customerCacheSynchronizer")
    private CacheSynchronizer<Customer> cacheSynchronizer;

    @PostConstruct
    public void init() {
        cacheSynchronizer.initialize();
    }

    @Override
    public Optional<Customer> findById(@NonNull Long id) {
        return cacheSynchronizer
                .getAll()
                .stream()
                .filter(customer -> customer.getId().equals(id))
                .findFirst();
    }

    @Override
    public Optional<Customer> findByUsername(@NonNull String username) {

        return cacheSynchronizer
                .getAll()
                .stream()
                .filter(customer -> customer.getUsername().equalsIgnoreCase(username))
                .findFirst();
    }

    @Override
    public List<Customer> findAll() {
        return new LinkedList<>(cacheSynchronizer.getAll());
    }

    @Override
    public Customer save(@NonNull Customer customer) {
        cacheSynchronizer.add(customer);
        return customer;
    }

    @Override
    public void delete(@NonNull Customer customer) {
        cacheSynchronizer.remove(customer);
    }

    @Override
    public void update(@NonNull Customer customer) {
        cacheSynchronizer.update(customer);
    }

    @Override
    public void refresh() {
        cacheSynchronizer.refresh();
    }
}
