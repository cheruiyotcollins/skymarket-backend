package com.gigster.skymarket.service;

import com.gigster.skymarket.dto.CustomerResponseDto;
import com.gigster.skymarket.dto.NewCustomerDto;
import com.gigster.skymarket.dto.ResponseDto;
import com.gigster.skymarket.model.Customer;
import com.gigster.skymarket.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomerService {
    @Autowired
    CustomerRepository customerRepository;

    public NewCustomerDto saveCustomer(NewCustomerDto newCustomer){
        Customer customer= new Customer();
        customer.setEmail(newCustomer.getEmail());
        customer.setGender(newCustomer.getGender());
        customer.setFullName(newCustomer.getFullName());
        customer.setPhoneNo(newCustomer.getPhoneNo());
        customerRepository.save(customer);
        return newCustomer;

    }
    public List<CustomerResponseDto> findAll(){
        List<CustomerResponseDto> customerResponseDtos= new ArrayList<>();
        List<Customer> customers= customerRepository.findAll();
        customers.forEach( customer->{
            CustomerResponseDto customerResponseDto= new CustomerResponseDto();
            customerResponseDto.setEmail(customer.getEmail());
            customerResponseDto.setGender(customer.getGender());
            customerResponseDto.setFullName(customer.getFullName());
            customerResponseDto.setPhoneNo(customer.getPhoneNo());
            customerResponseDtos.add(customerResponseDto);

        }

        );
        return  customerResponseDtos;
    }
}
