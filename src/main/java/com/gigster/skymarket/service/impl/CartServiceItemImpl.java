package com.gigster.skymarket.service.impl;

import com.gigster.skymarket.dto.CartItemDto;
import com.gigster.skymarket.dto.ResponseDto;
import com.gigster.skymarket.service.CartServiceItem;
import org.springframework.http.ResponseEntity;

import java.util.List;

public class CartServiceItemImpl implements CartServiceItem {
    @Override
    public ResponseEntity<ResponseDto> addItemToCart(Long cartId, CartItemDto cartItemDto) {
        return null;
    }

    @Override
    public ResponseEntity<List<CartItemDto>> getCartItems(Long cartId) {
        return null;
    }

    @Override
    public ResponseEntity<ResponseDto> updateCartItem(Long cartId, Long itemId, int quantity) {
        return null;
    }

    @Override
    public ResponseEntity<ResponseDto> removeItemFromCart(Long cartId, Long itemId) {
        return null;
    }

    @Override
    public ResponseEntity<ResponseDto> clearCart(Long cartId) {
        return null;
    }
}
