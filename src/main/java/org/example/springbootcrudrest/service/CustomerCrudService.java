package org.example.springbootcrudrest.service;

import org.example.springbootcrudrest.model.*;

import java.util.List;

public interface CustomerCrudService {

    CustomerResultDto getCustomerById(Long id);

    CustomerResultDto getCustomerByName(String name);

    CustomerResultDto createCustomer(CustomerCreateDto createDto);

    void updateCustomer(String name, CustomerUpdateDto updateDto);

    void deleteCustomer(CustomerDeleteDto deleteDto);

    List<CustomerResultDto> getAllCustomers();




}
