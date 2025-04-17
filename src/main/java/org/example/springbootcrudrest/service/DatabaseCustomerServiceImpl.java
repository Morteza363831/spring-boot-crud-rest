package org.example.springbootcrudrest.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.example.springbootcrudrest.model.Customer;
import org.example.springbootcrudrest.repository.db.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DatabaseCustomerServiceImpl implements CustomerPersistenceService {

    private final CustomerRepository customerRepository;

    @Override
    public Customer createCustomer(@NonNull Customer customer) {
        return customerRepository.save(customer);
    }

    @Override
    public void updateCustomer(@NonNull String name, @NonNull Customer customer) {

        final Optional<Customer> foundedCustomer = customerRepository.findByUsername(name);

        if (foundedCustomer.isPresent()) {
            customerRepository.save(customer);
        }
    }

    @Override
    public void deleteCustomer(Customer customer) {
        customerRepository.delete(customer);
    }
}
