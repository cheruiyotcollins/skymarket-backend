package com.gigster.skymarket.controller;

import com.gigster.skymarket.dto.CustomerDto;
import com.gigster.skymarket.dto.ResponseDto;
import com.gigster.skymarket.service.CustomerService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable;

@RestController
@RequestMapping(value = "/api/customers")
@Slf4j
public class CustomerController {
    @Autowired
    private CustomerService customerService;

    @PostMapping
    public ResponseEntity<ResponseDto> createCustomer(@Valid @RequestBody CustomerDto newCustomer) {
        return customerService.saveCustomer(newCustomer);
    }

    @GetMapping
    public ResponseEntity<ResponseDto> getAllCustomers(Pageable pageable) {
        return customerService.findAllCustomers(pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDto> getCustomerById(@PathVariable long id) {
        return customerService.findCustomerById(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseDto> updateCustomer(
            @PathVariable long id,
            @Valid @RequestBody CustomerDto updatedCustomer) {
        return customerService.updateCustomer(id, updatedCustomer);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDto> deleteCustomer(@PathVariable long id) {
        return customerService.deleteCustomerById(id);
    }
}
