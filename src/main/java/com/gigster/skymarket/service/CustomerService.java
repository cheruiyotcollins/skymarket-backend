package com.gigster.skymarket.service;

import com.gigster.skymarket.dto.NewCustomer;
import com.gigster.skymarket.dto.ResponseDto;
import com.gigster.skymarket.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {
    @Autowired
    CustomerRepository customerRepository;
    public ResponseDto saveCustomer(NewCustomer newCustomer){
        return new ResponseDto();

    }
}
