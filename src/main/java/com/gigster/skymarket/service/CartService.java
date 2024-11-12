package com.gigster.skymarket.service;

import com.gigster.skymarket.dto.CartDto;
import com.gigster.skymarket.dto.CartItemDto;
import com.gigster.skymarket.dto.ResponseDto;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface CartService {

    // Add an item to the cart with a specified quantity
    ResponseEntity<ResponseDto> addCart(CartDto cartDto);

    ResponseEntity<ResponseDto> getAllCarts(Pageable pageable);

    // Remove a specific item from the cart by product ID
    ResponseEntity<ResponseDto> removeItemFromCart(Long productId);

    // Clear all items from the cart
    @Transactional
    ResponseEntity<ResponseDto> clearCart(Long customerId);

    // Calculate the total price of all items in the cart
    double calculateTotalPrice();

    ResponseEntity<ResponseDto> findCartPerCustomer(String username);

}

