package org.example.springbootcrudrest.controller;

import lombok.RequiredArgsConstructor;
import org.example.springbootcrudrest.config.ResultApi;
import org.example.springbootcrudrest.model.CustomerCreateDto;
import org.example.springbootcrudrest.model.CustomerDeleteDto;
import org.example.springbootcrudrest.model.CustomerResultDto;
import org.example.springbootcrudrest.model.CustomerUpdateDto;
import org.example.springbootcrudrest.service.CustomerService;
import org.example.springbootcrudrest.utility.ApiConstants;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;


    @GetMapping
    public ResponseEntity<ResultApi<List<CustomerResultDto>>> getCustomers() {

        return buildResponse("success", customerService.getAllCustomers(), ApiConstants.CUSTOMERS_BASE_PATH, HttpStatus.OK);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<ResultApi<CustomerResultDto>> getCustomer(@PathVariable Long id) {

        final CustomerResultDto customer = customerService.getCustomerById(id);
        return buildResponse("success", customer, ApiConstants.CUSTOMERS_BASE_PATH+"/id/" + id, HttpStatus.OK);
    }

    @GetMapping("/{name}")
    public ResponseEntity<ResultApi<CustomerResultDto>> getCustomer(@PathVariable String name) {

        final CustomerResultDto customer = customerService.getCustomerByName(name);
        return buildResponse("success", customer, ApiConstants.CUSTOMERS_BASE_PATH+"/" + name, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ResultApi<CustomerResultDto>> createCustomer(@RequestBody CustomerCreateDto createDto) {

        final CustomerResultDto customer = customerService.createCustomer(createDto);
        return buildResponse("success", customer, ApiConstants.CUSTOMERS_BASE_PATH, HttpStatus.CREATED);
    }

    @PutMapping("/{name}")
    public ResponseEntity<ResultApi<Object>> updateCustomer(@PathVariable String name, @RequestBody CustomerUpdateDto updateDto) {

        customerService.updateCustomer(name, updateDto);
        return buildResponse("success", null, ApiConstants.CUSTOMERS_BASE_PATH+"/" + name, HttpStatus.OK);
    }

    @DeleteMapping()
    public ResponseEntity<ResultApi<Object>> deleteCustomer(@RequestBody CustomerDeleteDto deleteDto) {

        customerService.deleteCustomer(deleteDto);
        return buildResponse("success", null, ApiConstants.CUSTOMERS_BASE_PATH, HttpStatus.OK);
    }

    private <T> ResponseEntity<ResultApi<T>> buildResponse(String message, T data, String path, HttpStatus httpStatus) {
        return ResponseEntity.status(httpStatus).header("Content-Type", "application/json").body(new ResultApi<>(message, data, path));
    }
}
