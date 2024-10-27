package com.gigster.skymarket.service.impl;


import com.gigster.skymarket.dto.CartItemDto;
import com.gigster.skymarket.service.CartService;

import java.util.List;

public class CartServiceImpl implements CartService {


    @Override
    public void addItemToCart(Long productId, int quantity) {

    }

    @Override
    public void removeItemFromCart(Long productId) {

    }

    @Override
    public void clearCart() {

    }

    @Override
    public double calculateTotalPrice() {
        return 0;
    }

    @Override
    public List<CartItemDto> getAllCartItems() {
        return List.of();
    }

    @Override
    public void updateCartItemQuantity(Long productId, int quantity) {

    }

    @Override
    public CartItemDto getCartItemByProductId(Long productId) {
        return null;
    }
}
