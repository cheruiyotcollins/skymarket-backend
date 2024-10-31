package com.gigster.skymarket.service;

import com.gigster.skymarket.dto.CartDto;
import com.gigster.skymarket.dto.CartItemDto;
import com.gigster.skymarket.dto.ResponseDto;
import com.gigster.skymarket.model.User;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CartService {

    // Add an item to the cart with a specified quantity
    ResponseEntity<ResponseDto> addCart(CartDto cartDto);
    public ResponseEntity<ResponseDto> getAllCarts();

    // Remove a specific item from the cart by product ID
    void removeItemFromCart(Long productId);

    // Clear all items from the cart
    void clearCart();

    // Calculate the total price of all items in the cart
    double calculateTotalPrice();

    // Additional CRUD methods:

    // Retrieve all items currently in the cart
    ResponseEntity<ResponseDto> getAllCartItems();

    // Update the quantity of a specific item in the cart
    void updateCartItemQuantity(Long productId, int quantity);

    // Retrieve a single cart item by product ID
    CartItemDto getCartItemByProductId(Long productId);

    ResponseEntity<ResponseDto> findCartPerUser(String username);
}

