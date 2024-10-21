package com.gigster.skymarket.service;

import com.gigster.skymarket.dto.CustomerResponseDto;
import com.gigster.skymarket.dto.NewCustomerDto;
import com.gigster.skymarket.dto.ResponseDto;
import com.gigster.skymarket.model.Customer;
import com.gigster.skymarket.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomerService {
    @Autowired
    CustomerRepository customerRepository;

    ResponseDto responseDto;

    public ResponseEntity<ResponseDto> saveCustomer(NewCustomerDto newCustomer){
        try {
            Customer customer= new Customer();
            customer.setEmail(newCustomer.getEmail());
            customer.setGender(newCustomer.getGender());
            customer.setFullName(newCustomer.getFullName());
            customer.setPhoneNo(newCustomer.getPhoneNo());
            customerRepository.save(customer);
            responseDto.setStatus(HttpStatus.CREATED); // Use CREATED for successful creation
            responseDto.setDescription("Customer created successfully");
            responseDto.setPayload(newCustomer);
        } catch (Exception e) {
            responseDto.setStatus(HttpStatus.BAD_REQUEST);
            responseDto.setDescription("Something went wrong, please check your request and try again");
        }
       return  new ResponseEntity<>(responseDto, responseDto.getStatus());

    }
    public ResponseEntity<ResponseDto> findAll(){
        responseDto= new ResponseDto();
        List<CustomerResponseDto> customerResponseDtos= new ArrayList<>();
        List<Customer> customers= customerRepository.findAll();
        if(customers.isEmpty()){
            responseDto.setStatus(HttpStatus.NO_CONTENT);
            responseDto.setDescription("No Customer Found");
        }else{
        customers.forEach( customer->{
            CustomerResponseDto customerResponseDto= new CustomerResponseDto();
            customerResponseDto.setEmail(customer.getEmail());
            customerResponseDto.setGender(customer.getGender());
            customerResponseDto.setFullName(customer.getFullName());
            customerResponseDto.setPhoneNo(customer.getPhoneNo());
            customerResponseDtos.add(customerResponseDto);
        }
        );
            responseDto.setStatus(HttpStatus.FOUND);
            responseDto.setDescription("Customers Fetched Successfully");
            responseDto.setPayload(customerResponseDtos);
        }
        return  new ResponseEntity<>(responseDto, responseDto.getStatus());
    }

    public ResponseEntity<ResponseDto> findCustomerById(long id){
        responseDto= new ResponseDto();
        if(customerRepository.existsById(id)){
            Customer customer=customerRepository.findById(id).get();
            CustomerResponseDto customerResponseDto=CustomerResponseDto.builder()
                    .email(customer.getEmail())
                    .gender(customer.getGender())
                    .phoneNo(customer.getPhoneNo())
                    .fullName(customer.getFullName())
                    .build();
            responseDto.setStatus(HttpStatus.OK);
            responseDto.setDescription("Customer Info Found");
            responseDto.setPayload(customerResponseDto);
        }else {
            responseDto.setStatus(HttpStatus.NOT_FOUND);
            responseDto.setDescription("No Customer with provided Id Found");
        }
        return new ResponseEntity<>(responseDto, responseDto.getStatus());
    }

    public ResponseEntity<ResponseDto> deleteCustomerById(long id){
        responseDto=new ResponseDto();
        try{
            responseDto.setStatus(HttpStatus.OK);
            responseDto.setDescription("Customer Successfully Deleted");

        }catch(Exception e){
            responseDto.setStatus(HttpStatus.NOT_FOUND);
            responseDto.setDescription("Customer Not Found");
        }
        return new ResponseEntity<>(responseDto, responseDto.getStatus());
    }
}
