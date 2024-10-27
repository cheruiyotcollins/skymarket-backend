package com.gigster.skymarket.service;


import com.gigster.skymarket.dto.CartItemDto;
import com.gigster.skymarket.dto.ResponseDto;
import com.gigster.skymarket.model.Cart;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CartServiceItem {
    // Create: Add an item to the cart
    ResponseEntity<ResponseDto> addItemToCart(Cart cart, CartItemDto cartItemDto);

    // Read: Get all items in the cart
    ResponseEntity<List<CartItemDto>> getCartItems(Long cartId);

    // Update: Update an item quantity in the cart
    ResponseEntity<ResponseDto> updateCartItem(Long cartId, Long itemId, int quantity);

    // Delete: Remove an item from the cart
    ResponseEntity<ResponseDto> removeItemFromCart(Long cartId, Long itemId);

    // Delete: Clear all items from the cart
    ResponseEntity<ResponseDto> clearCart(Long cartId);
}
