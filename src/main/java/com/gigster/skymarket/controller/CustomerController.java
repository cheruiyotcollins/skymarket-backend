package com.gigster.skymarket.controller;

import com.gigster.skymarket.dto.CustomerResponseDto;
import com.gigster.skymarket.dto.NewCustomerDto;
import com.gigster.skymarket.dto.ResponseDto;
import com.gigster.skymarket.service.CustomerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value="/api/customer/")
@Slf4j
public class CustomerController {
    @Autowired
    CustomerService customerService;

    ResponseDto responseDto;

    @PostMapping()
    public ResponseEntity<ResponseDto> addCustomer(@RequestBody NewCustomerDto newCustomer){
        responseDto=new ResponseDto();
        try{
            customerService.saveCustomer(newCustomer);
            responseDto.setStatus(HttpStatus.ACCEPTED);
            responseDto.setDescription("Added customer successfully");
            responseDto.setPayload(newCustomer);
        }catch(Exception e){
            responseDto.setStatus(HttpStatus.BAD_REQUEST);
            responseDto.setDescription("Something went terribly wrong, please check your request and try again");
        }

    return new ResponseEntity<>(responseDto, responseDto.getStatus());
    }
    @GetMapping
    public ResponseEntity<ResponseDto> findAll(){
       List<CustomerResponseDto> customerResponseDtos= customerService.findAll();
       responseDto.setStatus(HttpStatus.OK);
       responseDto.setDescription("List of all customers");
       responseDto.setPayload(customerResponseDtos);

       return new ResponseEntity<>(responseDto, responseDto.getStatus());
    }
}
