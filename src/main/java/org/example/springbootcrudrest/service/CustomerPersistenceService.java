package org.example.springbootcrudrest.service;

import org.example.springbootcrudrest.model.Customer;

public interface CustomerPersistenceService {

    Customer createCustomer(Customer customer);
    void updateCustomer(String name, Customer customer);
    void deleteCustomer(Customer customer);
}
