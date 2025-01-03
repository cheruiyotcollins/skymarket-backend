package com.gigster.skymarket.controller;

import com.gigster.skymarket.dto.CustomerDto;
import com.gigster.skymarket.dto.ResponseDto;
import com.gigster.skymarket.service.CustomerService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/customers")
@Slf4j
public class CustomerController {
    @Autowired
    private CustomerService customerService;

    @PostMapping
    public ResponseEntity<ResponseDto> createCustomer(@Valid @RequestBody CustomerDto newCustomer) {
        return customerService.saveCustomer(newCustomer);
    }

    @GetMapping
    public ResponseEntity<ResponseDto> getAllCustomers(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sort", defaultValue = "id,asc") String sort) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort.split(",")));
        return customerService.getAllCustomers(pageable);
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
