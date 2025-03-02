package com.gigster.skymarket.unit.controller;

import com.gigster.skymarket.controller.OrderController;
import com.gigster.skymarket.dto.OrderDto;
import com.gigster.skymarket.dto.ResponseDto;
import com.gigster.skymarket.service.OrderService;
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

class OrderControllerTest {

    @Mock
    private OrderService orderService;

    @InjectMocks
    private OrderController orderController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createOrder_ShouldReturnResponseEntity() {
        // Arrange
        OrderDto newOrder = new OrderDto(); // Add fields as necessary
        ResponseDto responseDto = new ResponseDto(); // Add fields as necessary
        ResponseEntity<ResponseDto> expectedResponse = new ResponseEntity<>(responseDto, HttpStatus.CREATED);

        when(orderService.createOrder(newOrder)).thenReturn(expectedResponse);

        // Act
        ResponseEntity<ResponseDto> actualResponse = orderController.createOrder(newOrder);

        // Assert
        assertEquals(expectedResponse, actualResponse);
        verify(orderService, times(1)).createOrder(newOrder);
    }

    @Test
    void getOrderById_ShouldReturnOrder() {
        // Arrange
        long orderId = 1L;
        ResponseDto responseDto = new ResponseDto(); // Add fields as necessary
        ResponseEntity<ResponseDto> expectedResponse = new ResponseEntity<>(responseDto, HttpStatus.OK);

        when(orderService.getOrderById(orderId)).thenReturn(expectedResponse);

        // Act
        ResponseEntity<ResponseDto> actualResponse = orderController.getOrderById(orderId);

        // Assert
        assertEquals(expectedResponse, actualResponse);
        verify(orderService, times(1)).getOrderById(orderId);
    }

    @Test
    void getAllOrders_ShouldReturnOrders() {
        // Arrange
        int page = 0;
        int size = 10;
        String sort = "id,asc";
        ResponseDto responseDto = new ResponseDto(); // Populate fields as necessary
        ResponseEntity<ResponseDto> expectedResponse = new ResponseEntity<>(responseDto, HttpStatus.OK);

        when(orderService.getAllOrders(page, size, sort)).thenReturn(expectedResponse);

        // Act
        ResponseEntity<ResponseDto> actualResponse = orderController.getAllOrders(page, size, sort);

        // Assert
        assertEquals(expectedResponse, actualResponse);
        verify(orderService, times(1)).getAllOrders(page, size, sort);
    }

    @Test
    void updateOrder_ShouldReturnUpdatedOrder() {
        // Arrange
        long orderId = 1L;
        OrderDto updatedOrder = new OrderDto(); // Add fields as necessary
        ResponseDto responseDto = new ResponseDto(); // Add fields as necessary
        ResponseEntity<ResponseDto> expectedResponse = new ResponseEntity<>(responseDto, HttpStatus.OK);

        when(orderService.updateOrder(orderId, updatedOrder)).thenReturn(expectedResponse);

        // Act
        ResponseEntity<ResponseDto> actualResponse = orderController.updateOrder(orderId, updatedOrder);

        // Assert
        assertEquals(expectedResponse, actualResponse);
        verify(orderService, times(1)).updateOrder(orderId, updatedOrder);
    }

    @Test
    void cancelOrder_ShouldReturnCanceledOrder() {
        // Arrange
        long orderId = 1L;
        ResponseDto responseDto = new ResponseDto(); // Add fields as necessary
        ResponseEntity<ResponseDto> expectedResponse = new ResponseEntity<>(responseDto, HttpStatus.OK);

        when(orderService.cancelOrder(orderId)).thenReturn(expectedResponse);

        // Act
        ResponseEntity<ResponseDto> actualResponse = orderController.cancelOrder(orderId);

        // Assert
        assertEquals(expectedResponse, actualResponse);
        verify(orderService, times(1)).cancelOrder(orderId);
    }

    @Test
    void deleteOrder_ShouldReturnResponseEntity() {
        // Arrange
        long orderId = 1L;
        ResponseDto responseDto = new ResponseDto(); // Add fields as necessary
        ResponseEntity<ResponseDto> expectedResponse = new ResponseEntity<>(responseDto, HttpStatus.NO_CONTENT);

        when(orderService.deleteOrder(orderId)).thenReturn(expectedResponse);

        // Act
        ResponseEntity<ResponseDto> actualResponse = orderController.deleteOrder(orderId);

        // Assert
        assertEquals(expectedResponse, actualResponse);
        verify(orderService, times(1)).deleteOrder(orderId);
    }
}
