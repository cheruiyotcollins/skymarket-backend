package com.gigster.skymarket.service;

import com.gigster.skymarket.dto.ResponseDto;
import com.gigster.skymarket.security.UserPrincipal;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;

public interface CartService {

    ResponseEntity<ResponseDto> createCart(UserPrincipal userPrincipal);

    ResponseEntity<ResponseDto> getAllCarts(int page, int size, String sort, UserPrincipal userPrincipal);

    // Remove a specific item from the cart by product ID
    ResponseEntity<ResponseDto> removeItemFromCart(Long productId);

    // Clear all items from the cart
    @Transactional
    ResponseEntity<ResponseDto> clearCart(Long customerId);

    // Calculate the total price of all items in the cart
    double calculateTotalPrice();

    ResponseEntity<ResponseDto> findCartPerCustomer(String username);

}