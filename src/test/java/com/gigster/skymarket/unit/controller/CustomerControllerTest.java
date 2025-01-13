package com.gigster.skymarket.unit.controller;

import com.gigster.skymarket.controller.CustomerController;
import com.gigster.skymarket.dto.CustomerDto;
import com.gigster.skymarket.dto.ResponseDto;
import com.gigster.skymarket.service.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class CustomerControllerTest {

    @Mock
    private CustomerService customerService;

    @InjectMocks
    private CustomerController customerController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createCustomer_ShouldReturnResponseEntity() {
        // Arrange
        CustomerDto newCustomer = new CustomerDto(); // Add fields as necessary
        ResponseDto responseDto = new ResponseDto(); // Add fields as necessary
        ResponseEntity<ResponseDto> expectedResponse = new ResponseEntity<>(responseDto, HttpStatus.CREATED);

        when(customerService.saveCustomer(newCustomer)).thenReturn(expectedResponse);

        // Act
        ResponseEntity<ResponseDto> actualResponse = customerController.createCustomer(newCustomer);

        // Assert
        assertEquals(expectedResponse, actualResponse);
        verify(customerService, times(1)).saveCustomer(newCustomer);
    }

    @Test
    void getCustomerById_ShouldReturnCustomer() {
        // Arrange
        long customerId = 1L;
        ResponseDto responseDto = new ResponseDto(); // Add fields as necessary
        ResponseEntity<ResponseDto> expectedResponse = new ResponseEntity<>(responseDto, HttpStatus.OK);

        when(customerService.findCustomerById(customerId)).thenReturn(expectedResponse);

        // Act
        ResponseEntity<ResponseDto> actualResponse = customerController.getCustomerById(customerId);

        // Assert
        assertEquals(expectedResponse, actualResponse);
        verify(customerService, times(1)).findCustomerById(customerId);
    }

    @Test
    void getAllCustomers_ShouldReturnCustomers() {
        // Arrange
        int page = 0;
        int size = 10;
        String sort = "id,asc";
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort.split(",")));
        ResponseDto responseDto = new ResponseDto(); // Add fields as necessary
        ResponseEntity<ResponseDto> expectedResponse = new ResponseEntity<>(responseDto, HttpStatus.OK);

        when(customerService.getAllCustomers(pageable)).thenReturn(expectedResponse);

        // Act
        ResponseEntity<ResponseDto> actualResponse = customerController.getAllCustomers(page, size, sort);

        // Assert
        assertEquals(expectedResponse, actualResponse);
        verify(customerService, times(1)).getAllCustomers(pageable);
    }

    @Test
    void updateCustomer_ShouldReturnUpdatedCustomer() {
        // Arrange
        long customerId = 1L;
        CustomerDto updatedCustomer = new CustomerDto(); // Add fields as necessary
        ResponseDto responseDto = new ResponseDto(); // Add fields as necessary
        ResponseEntity<ResponseDto> expectedResponse = new ResponseEntity<>(responseDto, HttpStatus.OK);

        when(customerService.updateCustomer(customerId, updatedCustomer)).thenReturn(expectedResponse);

        // Act
        ResponseEntity<ResponseDto> actualResponse = customerController.updateCustomer(customerId, updatedCustomer);

        // Assert
        assertEquals(expectedResponse, actualResponse);
        verify(customerService, times(1)).updateCustomer(customerId, updatedCustomer);
    }

    @Test
    void deleteCustomer_ShouldReturnResponseEntity() {
        // Arrange
        long customerId = 1L;
        ResponseDto responseDto = new ResponseDto(); // Add fields as necessary
        ResponseEntity<ResponseDto> expectedResponse = new ResponseEntity<>(responseDto, HttpStatus.NO_CONTENT);

        when(customerService.deleteCustomerById(customerId)).thenReturn(expectedResponse);

        // Act
        ResponseEntity<ResponseDto> actualResponse = customerController.deleteCustomer(customerId);

        // Assert
        assertEquals(expectedResponse, actualResponse);
        verify(customerService, times(1)).deleteCustomerById(customerId);
    }
}
