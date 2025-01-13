package com.gigster.skymarket.service.impl;

import com.gigster.skymarket.dto.CustomerResponseDto;
import com.gigster.skymarket.dto.CustomerDto;
import com.gigster.skymarket.dto.ResponseDto;
import com.gigster.skymarket.mapper.CustomerMapper;
import com.gigster.skymarket.model.Customer;
import com.gigster.skymarket.repository.CustomerRepository;
import com.gigster.skymarket.service.CustomerService;
import com.gigster.skymarket.mapper.ResponseDtoMapper;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    ResponseDtoMapper responseDtoSetter;

    @Autowired
    CustomerMapper customerMapper;

    @Override
    public ResponseEntity<ResponseDto> saveCustomer(CustomerDto newCustomer){
        try {
            Customer customer= Customer.builder()
                    .email(newCustomer.getEmail())
                    .gender(newCustomer.getGender())
                    .fullName(newCustomer.getFullName())
                    .phoneNo(newCustomer.getPhoneNo())
                    .build();
            customerRepository.save(customer);
            return responseDtoSetter.responseDtoSetter(HttpStatus.CREATED,"Customer created successfully",newCustomer);
        } catch (Exception e) {
            return responseDtoSetter.responseDtoSetter(HttpStatus.BAD_REQUEST,"Something went wrong, please check your request and try again");
        }

    }

    @Override
    public ResponseEntity<ResponseDto> findCustomerById(long id){
        if(customerRepository.existsById(id)){
            Customer customer=customerRepository.findById(id).get();
            //calling mapCustomerResponseDto
            CustomerResponseDto customerResponseDto=mapCustomerResponseDto(customer);
            return responseDtoSetter.responseDtoSetter(HttpStatus.FOUND,"Customer Info Found",customerResponseDto);

        }else {
            return responseDtoSetter.responseDtoSetter(HttpStatus.NOT_FOUND,"No Customer with provided Id Found");

        }
    }

    @Override
    public ResponseEntity<ResponseDto> getAllCustomers(Pageable pageable) {
        Page<Customer> customersPage = customerRepository.findAll(pageable);
        List<CustomerDto> customerDtos = customersPage.getContent()
                .stream()
                .map(customerMapper::toDto)
                .collect(Collectors.toList());

        ResponseDto responseDto = ResponseDto.builder()
                .status(HttpStatus.OK)
                .description("List of All Customers.")
                .payload(customerDtos)
                .totalPages(customersPage.getTotalPages())
                .totalElements(customersPage.getTotalElements())
                .currentPage(customersPage.getNumber())
                .pageSize(customersPage.getSize())
                .build();

        return ResponseEntity.ok(responseDto);
    }

    @Override
    public ResponseEntity<ResponseDto> deleteCustomerById(long id){
        try{
            return responseDtoSetter.responseDtoSetter(HttpStatus.OK,"Customer Successfully Deleted");

        }catch(Exception e){
            return responseDtoSetter.responseDtoSetter(HttpStatus.NOT_FOUND,"Customer Not Found");
        }

    }

    @Override
    public ResponseEntity<ResponseDto> updateCustomer(long id, @Valid CustomerDto updatedCustomer) {
        try {
            Optional<Customer> customerOpt = customerRepository.findById(id);
            if (customerOpt.isPresent()) {
                Customer existingCustomer = customerOpt.get();

                Customer updatedCustomerEntity = Customer.builder()
                        .customerId(existingCustomer.getCustomerId()) // Retain the existing ID
                        .email(updatedCustomer.getEmail())
                        .gender(updatedCustomer.getGender())
                        .fullName(updatedCustomer.getFullName())
                        .phoneNo(updatedCustomer.getPhoneNo())
                        .build();

                customerRepository.save(updatedCustomerEntity);
                return responseDtoSetter.responseDtoSetter(HttpStatus.OK, "Customer updated successfully", updatedCustomer);
            } else {
                return responseDtoSetter.responseDtoSetter(HttpStatus.NOT_FOUND, "Customer not found");
            }
        } catch (Exception e) {
            return responseDtoSetter.responseDtoSetter(HttpStatus.BAD_REQUEST, "Something went wrong, please check your request and try again");
        }
    }

    private CustomerResponseDto mapCustomerResponseDto(Customer customer){
        return CustomerResponseDto.builder()
                .email(customer.getEmail())
                .gender(customer.getGender())
                .phoneNo(customer.getPhoneNo())
                .fullName(customer.getFullName())
                .build();
    }
}