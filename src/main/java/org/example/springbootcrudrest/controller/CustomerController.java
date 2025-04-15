package org.example.springbootcrudrest.controller;

import lombok.RequiredArgsConstructor;
import org.example.springbootcrudrest.config.ResultApi;
import org.example.springbootcrudrest.model.CustomerCreateDto;
import org.example.springbootcrudrest.model.CustomerDeleteDto;
import org.example.springbootcrudrest.model.CustomerResultDto;
import org.example.springbootcrudrest.model.CustomerUpdateDto;
import org.example.springbootcrudrest.service.CustomerService;
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

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResultApi<>("success", customerService.getAllCustomers(), "/api/v1/customers"));
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<ResultApi<CustomerResultDto>> getCustomer(@PathVariable Long id) {

        final CustomerResultDto customer = customerService.getCustomerById(id);
        if (customer == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new ResultApi<>("fail", null, "/customers/id/" + id));
        }

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResultApi<>("success", customer, "/api/v1/customers/id/" + id));
    }

    @GetMapping("/{name}")
    public ResponseEntity<ResultApi<CustomerResultDto>> getCustomer(@PathVariable String name) {

        final CustomerResultDto customer = customerService.getCustomerByName(name);

        if (customer == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new ResultApi<>("fail", null, "/customers/" + name));
        }

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResultApi<>("success", customer, "/api/v1/customers/" + name));
    }

    @PostMapping
    public ResponseEntity<ResultApi<CustomerResultDto>> createCustomer(@RequestBody CustomerCreateDto createDto) {

        final CustomerResultDto customer = customerService.createCustomer(createDto);

        if (customer == null) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ResultApi<>("fail", null, "/customers"));
        }

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResultApi<>("success", customer, "/api/v1/customers"));
    }

    @PutMapping("/{name}")
    public ResponseEntity<ResultApi<?>> updateCustomer(@PathVariable String name, @RequestBody CustomerUpdateDto updateDto) {

        customerService.updateCustomer(name, updateDto);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResultApi<>("success", null, "/customers/" + name));
    }

    @DeleteMapping()
    public ResponseEntity<ResultApi<?>> deleteCustomer(@RequestBody CustomerDeleteDto deleteDto) {

        customerService.deleteCustomer(deleteDto);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResultApi<>("success", null, "/customers"));
    }
}
