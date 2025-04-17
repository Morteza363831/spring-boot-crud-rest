package org.example.springbootcrudrest.repository.inMem;

import lombok.RequiredArgsConstructor;
import org.example.springbootcrudrest.exception.DuplicateEntityException;
import org.example.springbootcrudrest.exception.NotFoundException;
import org.example.springbootcrudrest.model.Customer;
import org.example.springbootcrudrest.repository.db.CustomerRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CustomerCacheSynchronizerImpl implements CacheSynchronizer<Customer> {


    private final List<Customer> customers;

    private final CustomerRepository customerRepository;


    @Override
    public void initialize() {
        customers.addAll(customerRepository.findAll());
    }

    @Override
    public void refresh() {
        clear();
        initialize();
    }

    @Override
    public void clear() {
        customers.clear();
    }

    @Override
    public void add(Customer customer) {
        customers
                .stream()
                .filter(c -> c.getId().equals(customer.getId()))
                .findFirst()
                .ifPresentOrElse(
                        c -> {
                            throw new DuplicateEntityException(customer.getUsername());
                        },
                        () -> customers.add(customer));
    }

    @Override
    public void remove(Customer customer) {
        customers.removeIf(c -> c.getId().equals(customer.getId()));
    }

    @Override
    public void update(Customer customer) {
        customers
                .stream()
                .filter(c -> c.getId().equals(customer.getId()))
                .findFirst()
                .ifPresentOrElse(
                        c -> {
                            customers.remove(c);
                            customers.add(customer);
                        },
                        () -> {
                            throw new NotFoundException(customer.getUsername());
                        });
    }
}
