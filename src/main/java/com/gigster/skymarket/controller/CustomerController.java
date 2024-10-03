package com.gigster.skymarket.controller;

import com.gigster.skymarket.dto.NewCustomer;
import com.gigster.skymarket.dto.ResponseDto;
import com.gigster.skymarket.service.CustomerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value="/api/customer/")
@Slf4j
public class CustomerController {
    @Autowired
    CustomerService customerService;
    @PostMapping()
    public ResponseEntity<ResponseDto> addCustomer(@RequestBody NewCustomer newCustomer){
        ResponseDto responseDto=new ResponseDto();
    return new ResponseEntity<>(responseDto, HttpStatus.ACCEPTED);
    }
}
