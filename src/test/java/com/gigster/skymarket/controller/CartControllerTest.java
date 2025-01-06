package com.gigster.skymarket.controller;

import com.gigster.skymarket.dto.ResponseDto;
import com.gigster.skymarket.security.UserPrincipal;
import com.gigster.skymarket.service.CartService;
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
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class CartControllerTest {

    @Mock
    private CartService cartService;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private CartController cartController;

    private UserPrincipal mockUserPrincipal;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Setup mock user principal
        mockUserPrincipal = new UserPrincipal(
                1L,                      // id
                "Kib Van Hag",              // name
                "kibhag",               // username
                "kib.hag@example.com",  // email
                "1234567890",            // phoneNo
                "password",              // password
                List.of(new SimpleGrantedAuthority("ROLE_CUSTOMER")) // authorities
        );

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(mockUserPrincipal);

        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void addCart_ShouldReturnResponseEntity() {
        // Arrange
        ResponseDto responseDto = new ResponseDto(); // Set fields as necessary
        responseDto.setStatus(HttpStatus.valueOf(HttpStatus.CREATED.value()));
        ResponseEntity<ResponseDto> expectedResponse = ResponseEntity.status(HttpStatus.CREATED).body(responseDto);

        when(cartService.addCart(mockUserPrincipal)).thenReturn(expectedResponse);

        // Act
        ResponseEntity<ResponseDto> actualResponse = cartController.addCart();

        // Assert
        assertEquals(expectedResponse, actualResponse);
        verify(cartService, times(1)).addCart(mockUserPrincipal);
    }

    @Test
    void getAllCarts_ShouldReturnResponseEntity() {
        // Arrange
        int page = 0;
        int size = 10;
        String sort = "id,asc";
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort.split(",")));
        ResponseDto responseDto = new ResponseDto(); // Set fields as necessary
        ResponseEntity<ResponseDto> expectedResponse = new ResponseEntity<>(responseDto, HttpStatus.OK);

        when(cartService.getAllCarts(pageable, mockUserPrincipal)).thenReturn(expectedResponse);

        // Act
        ResponseEntity<ResponseDto> actualResponse = cartController.getAllCarts(page, size, sort);

        // Assert
        assertEquals(expectedResponse, actualResponse);
        verify(cartService, times(1)).getAllCarts(pageable, mockUserPrincipal);
    }

    @Test
    void findCartByCustomerId_ShouldReturnResponseEntity() {
        // Arrange
        String username = "john.doe@example.com";
        ResponseDto responseDto = new ResponseDto(); // Set fields as necessary
        ResponseEntity<ResponseDto> expectedResponse = new ResponseEntity<>(responseDto, HttpStatus.OK);

        when(authentication.getName()).thenReturn(username);
        when(cartService.findCartPerCustomer(username)).thenReturn(expectedResponse);

        // Act
        ResponseEntity<ResponseDto> actualResponse = cartController.findCartByCustomerId(authentication);

        // Assert
        assertEquals(expectedResponse, actualResponse);
        verify(cartService, times(1)).findCartPerCustomer(username);
    }

    @Test
    void removeItemFromCart_ShouldReturnResponseEntity() {
        // Arrange
        Long productId = 123L;
        ResponseDto responseDto = new ResponseDto(); // Set fields as necessary
        ResponseEntity<ResponseDto> expectedResponse = new ResponseEntity<>(responseDto, HttpStatus.OK);

        when(cartService.removeItemFromCart(productId)).thenReturn(expectedResponse);

        // Act
        ResponseEntity<ResponseDto> actualResponse = cartController.removeItemFromCart(productId);

        // Assert
        assertEquals(expectedResponse, actualResponse);
        verify(cartService, times(1)).removeItemFromCart(productId);
    }

    @Test
    void clearCart_ShouldReturnResponseEntity() {
        // Arrange
        Long customerId = 1L;
        ResponseDto responseDto = new ResponseDto(); // Set fields as necessary
        ResponseEntity<ResponseDto> expectedResponse = new ResponseEntity<>(responseDto, HttpStatus.NO_CONTENT);

        when(cartService.clearCart(customerId)).thenReturn(expectedResponse);

        // Act
        ResponseEntity<ResponseDto> actualResponse = cartController.clearCart(customerId);

        // Assert
        assertEquals(expectedResponse, actualResponse);
        verify(cartService, times(1)).clearCart(customerId);
    }
}
