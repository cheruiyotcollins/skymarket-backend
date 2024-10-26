package com.gigster.skymarket.service;

import com.gigster.skymarket.dto.CustomerResponseDto;
import com.gigster.skymarket.dto.NewCustomerDto;
import com.gigster.skymarket.dto.ResponseDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CustomerService{

    ResponseEntity<ResponseDto> saveCustomer(NewCustomerDto newCustomer);

    ResponseEntity<ResponseDto> findAll();

    ResponseEntity<ResponseDto> findCustomerById(long id);

    ResponseEntity<ResponseDto> deleteCustomerById(long id);

}
