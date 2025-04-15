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
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final InMemoryCustomerRepository inMemoryCustomerRepository;
    private final Validator validator;


    @Override
    public CustomerResultDto getCustomerById(@NonNull Long id) {

        final Optional<Customer> foundedCustomer = customerRepository.findById(id);
        return foundedCustomer
                .map(CustomerMapper.INSTANCE::toDto)
                .orElseThrow(() -> new NotFoundException(id+""));
    }

    @Override
    public CustomerResultDto getCustomerByName(@NonNull String name) {

        final Optional<Customer> foundedCustomer = inMemoryCustomerRepository.findByUsername(name);
        return foundedCustomer
                .map(CustomerMapper.INSTANCE::toDto)
                .orElseThrow(() -> new NotFoundException(name));
    }

    @Override
    public CustomerResultDto createCustomer(@NonNull CustomerCreateDto createDto) {

        validator.validate(createDto);

        final Optional<Customer> foundedCustomer = inMemoryCustomerRepository.findByUsername(createDto.getUsername());

        foundedCustomer.ifPresent(customer -> {
            throw new DuplicateEntityException(createDto.getUsername());
        });

        final Customer persistedCustomer = customerRepository.save(CustomerMapper.INSTANCE.toEntity(createDto));

        return CustomerMapper.INSTANCE.toDto(persistedCustomer);
    }

    @Override
    public void updateCustomer(@NonNull String name, @NonNull CustomerUpdateDto updateDto) {

        validator.validate(updateDto);

        final Optional<Customer> foundedCustomer = customerRepository.findByUsername(name);

        foundedCustomer.ifPresentOrElse(
                customer -> {
                    CustomerMapper.INSTANCE.partialUpdate(updateDto, customer);
                    customerRepository.save(customer);
                    inMemoryCustomerRepository.update(customer);
                }, () -> {
                    throw new NotFoundException(name);
                });
    }

    @Override
    public void deleteCustomer(@NonNull CustomerDeleteDto deleteDto) {

        validator.validate(deleteDto);

        final Optional<Customer> customerOptional = inMemoryCustomerRepository.findByUsername(deleteDto.getUsername());

        customerOptional.ifPresentOrElse(
                customerRepository::delete,
                () -> {
                    throw new NotFoundException(deleteDto.getUsername());
                });
    }

    @Override
    public List<CustomerResultDto> getAllCustomers() {

        final List<Customer> customers = inMemoryCustomerRepository.findAll();
        final List<CustomerResultDto> resultDtos = new LinkedList<>();
        customers.forEach(customer -> {
            resultDtos.add(CustomerMapper.INSTANCE.toDto(customer));
        });
        return resultDtos;
    }

    @Override
    public void refreshCustomers() {
        inMemoryCustomerRepository.refresh();
    }
}
