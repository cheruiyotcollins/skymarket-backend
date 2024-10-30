package com.gigster.skymarket.service.impl;


import com.gigster.skymarket.dto.CartDto;
import com.gigster.skymarket.dto.CartItemDto;
import com.gigster.skymarket.dto.ResponseDto;
import com.gigster.skymarket.model.Cart;
import com.gigster.skymarket.model.CartItem;
import com.gigster.skymarket.model.User;
import com.gigster.skymarket.repository.CartItemRepository;
import com.gigster.skymarket.repository.CartRepository;
import com.gigster.skymarket.service.CartService;
import com.gigster.skymarket.setter.ResponseDtoSetter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartServiceImpl implements CartService {
    @Autowired
    CartRepository cartRepository;
    @Autowired
    CartItemRepository cartItemRepository;

    @Autowired
    ResponseDtoSetter responseDtoSetter;

    @Override
    public ResponseEntity<ResponseDto> addCart(CartDto cartDto) {
            Cart cart= new Cart();
            cart.setUser(cartDto.getUser());
        cartRepository.save(cart);
            return responseDtoSetter.responseDtoSetter(HttpStatus.CREATED,"Cart Added Successfully", cart);

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
