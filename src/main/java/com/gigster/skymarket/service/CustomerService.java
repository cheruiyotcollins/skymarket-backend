package com.gigster.skymarket.service;

import com.gigster.skymarket.dto.CustomerDto;
import com.gigster.skymarket.dto.ResponseDto;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;

public interface CustomerService{

    ResponseEntity<ResponseDto> saveCustomer(CustomerDto newCustomer);

    ResponseEntity<ResponseDto> findAll();

    ResponseEntity<ResponseDto> findCustomerById(long id);

    ResponseEntity<ResponseDto> deleteCustomerById(long id);

    ResponseEntity<ResponseDto> updateCustomer(long id, @Valid CustomerDto updatedCustomer);
}
