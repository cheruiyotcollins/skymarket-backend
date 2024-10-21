package com.gigster.skymarket.controller;

import com.gigster.skymarket.dto.CustomerResponseDto;
import com.gigster.skymarket.dto.NewCustomerDto;
import com.gigster.skymarket.dto.ResponseDto;
import com.gigster.skymarket.service.CustomerService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/customers")
@Slf4j
public class CustomerController {
    @Autowired
    CustomerService customerService;

    @PostMapping
    public ResponseEntity<ResponseDto> createCustomer(@Valid @RequestBody NewCustomerDto newCustomer) {

        return customerService.saveCustomer(newCustomer);
    }

    @GetMapping
    public ResponseEntity<ResponseDto> getAllCustomers() {
        return customerService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDto> getCustomerById(@PathVariable long id) {
        // Presumably, you'll have logic here to build the response
        return customerService.findCustomerById(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDto> deleteCustomer(@PathVariable long id) {
        return customerService.deleteCustomerById(id);
    }
}
