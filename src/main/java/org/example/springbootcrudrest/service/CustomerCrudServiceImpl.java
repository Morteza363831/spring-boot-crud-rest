package org.example.springbootcrudrest.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.example.springbootcrudrest.exception.DuplicateEntityException;
import org.example.springbootcrudrest.exception.NotFoundException;
import org.example.springbootcrudrest.model.*;
import org.example.springbootcrudrest.repository.db.CustomerRepository;
import org.example.springbootcrudrest.repository.inMem.InMemoryCustomerRepository;
import org.example.springbootcrudrest.utility.Validator;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerCrudServiceImpl implements CustomerCrudService {

    private final CustomerRepository customerRepository;
    private final InMemoryCustomerRepository inMemoryCustomerRepository;
    private final DatabaseCustomerServiceImpl databaseCustomerService;
    private final InMemoryCustomerServiceImpl inMemoryCustomerServiceImpl;
    private final Validator validator;
    private final CustomerMapper customerMapper;



    @Override
    public CustomerResultDto getCustomerById(@NonNull Long id) {

        final Optional<Customer> foundedCustomer = inMemoryCustomerRepository.findById(id);
        return foundedCustomer
                .map(customerMapper::toDto)
                .orElseThrow(() -> new NotFoundException(id+""));
    }

    @Override
    public CustomerResultDto getCustomerByName(@NonNull String name) {

        final Optional<Customer> foundedCustomer = inMemoryCustomerRepository.findByUsername(name);
        return foundedCustomer
                .map(customerMapper::toDto)
                .orElseThrow(() -> new NotFoundException(name));
    }

    @Override
    public CustomerResultDto createCustomer(@NonNull CustomerCreateDto createDto) {

        validator.validate(createDto);

        final Optional<Customer> foundedCustomer = inMemoryCustomerRepository.findByUsername(createDto.getUsername());

        foundedCustomer.ifPresent(customer -> {
            throw new DuplicateEntityException(createDto.getUsername());
        });

        final Customer persistedCustomer = databaseCustomerService.createCustomer(customerMapper.toEntity(createDto));

        return customerMapper.toDto(inMemoryCustomerServiceImpl.createCustomer(persistedCustomer));
    }

    @Override
    public void updateCustomer(@NonNull String name, @NonNull CustomerUpdateDto updateDto) {

        validator.validate(updateDto);

        final Optional<Customer> foundedCustomer = inMemoryCustomerRepository.findByUsername(name);

        foundedCustomer.ifPresentOrElse(
                customer -> {
                    customerMapper.partialUpdate(updateDto, customer);
                    databaseCustomerService.updateCustomer(name, customer);
                    inMemoryCustomerServiceImpl.updateCustomer(name, customer);
                }, () -> {
                    throw new NotFoundException(name);
                });
    }

    @Override
    public void deleteCustomer(@NonNull CustomerDeleteDto deleteDto) {

        validator.validate(deleteDto);

        final Optional<Customer> customerOptional = inMemoryCustomerRepository.findByUsername(deleteDto.getUsername());

        customerOptional.ifPresentOrElse(
                customer -> {
                    databaseCustomerService.deleteCustomer(customer);
                    inMemoryCustomerServiceImpl.deleteCustomer(customer);
                },
                () -> {
                    throw new NotFoundException(deleteDto.getUsername());
                });
    }

    @Override
    public List<CustomerResultDto> getAllCustomers() {

        final List<Customer> customers = inMemoryCustomerRepository.findAll();
        final List<CustomerResultDto> resultDtos = new LinkedList<>();
        customers.forEach(customer -> {
            resultDtos.add(customerMapper.toDto(customer));
        });
        return resultDtos;
    }


}
