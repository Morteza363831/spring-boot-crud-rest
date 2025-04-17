package org.example.springbootcrudrest.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.example.springbootcrudrest.model.Customer;
import org.example.springbootcrudrest.repository.inMem.InMemoryCustomerRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InMemoryCustomerServiceImpl implements CustomerPersistenceService {

    private final InMemoryCustomerRepository customerRepository;

    @Override
    public Customer createCustomer(@NonNull Customer customer) {
        return customerRepository.save(customer);
    }

    @Override
    public void updateCustomer(String name,@NonNull Customer customer) {
        customerRepository.update(customer);
    }

    @Override
    public void deleteCustomer(Customer customer) {
        customerRepository.delete(customer);
    }
}
