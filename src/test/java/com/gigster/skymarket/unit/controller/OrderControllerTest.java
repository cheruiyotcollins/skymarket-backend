package com.gigster.skymarket.unit.controller;

import com.gigster.skymarket.controller.OrderController;
import com.gigster.skymarket.dto.NewOrderDto;
import com.gigster.skymarket.dto.OrderDto;
import com.gigster.skymarket.dto.ResponseDto;
import com.gigster.skymarket.model.Cart;
import com.gigster.skymarket.repository.CartRepository;
import com.gigster.skymarket.security.UserPrincipal;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class OrderControllerTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private OrderService orderService;

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    @InjectMocks
    private OrderController orderController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

//    @Test
//    void createOrder_ShouldReturnResponseEntity() {
//        // Arrange
//        NewOrderDto newOrderDto = new NewOrderDto();
//        newOrderDto.setPaymentMethod("Credit Card");
//        newOrderDto.setShippingAddress("123 Main St");
//
//        UserPrincipal userPrincipal = mock(UserPrincipal.class);
//        when(authentication.getPrincipal()).thenReturn(userPrincipal);
//        when(userPrincipal.getCustomerId()).thenReturn(1L);
//
//        Cart cart = new Cart();
//        cart.setCartId(100L);
//        when(cartRepository.findByCustomerId(1L)).thenReturn(Optional.of(cart));
//
//        OrderDto orderDto = new OrderDto();
//        orderDto.setCustomerId(1L);
//        orderDto.setCartId(100L);
//        orderDto.setPaymentMethod("Credit Card");
//        orderDto.setShippingAddress("123 Main St");
//
//        ResponseDto responseDto = new ResponseDto();
//        ResponseEntity<ResponseDto> expectedResponse = new ResponseEntity<>(responseDto, HttpStatus.CREATED);
//        when(orderService.createOrder(orderDto)).thenReturn(expectedResponse);
//
//        // Act
//        ResponseEntity<ResponseDto> actualResponse = orderController.createOrder(newOrderDto);
//
//        // Assert
//        assertEquals(expectedResponse, actualResponse);
//        verify(cartRepository, times(1)).findByCustomerId(1L);
//        verify(orderService, times(1)).createOrder(orderDto);
//    }

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
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort.split(",")));
        ResponseDto responseDto = new ResponseDto(); // Add fields as necessary
        ResponseEntity<ResponseDto> expectedResponse = new ResponseEntity<>(responseDto, HttpStatus.OK);

        when(orderService.getAllOrders(pageable)).thenReturn(expectedResponse);

        // Act
        ResponseEntity<ResponseDto> actualResponse = orderController.getAllOrders(pageable);

        // Assert
        assertEquals(expectedResponse, actualResponse);
        verify(orderService, times(1)).getAllOrders(pageable);
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
