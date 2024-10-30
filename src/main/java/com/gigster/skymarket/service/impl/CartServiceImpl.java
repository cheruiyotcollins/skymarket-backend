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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
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
    public ResponseEntity<ResponseDto> getAllCarts() {
        return responseDtoSetter.responseDtoSetter(HttpStatus.OK,"List Of Carts ", cartRepository.findAll());

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
    public ResponseEntity<ResponseDto> getAllCartItems() {
        return responseDtoSetter.responseDtoSetter(HttpStatus.OK,"List Of All Carts", cartItemRepository.findAll());
    }

    @Override
    public void updateCartItemQuantity(Long productId, int quantity) {

    }

    @Override
    public CartItemDto getCartItemByProductId(Long productId) {
        return null;
    }

    @Override
    public ResponseEntity<ResponseDto> findCartPerUserId(User user) {
        log.info("fetching cart per User::::::::::::::::::::::::::::::::::::::::::::::::::::::::::");
        return responseDtoSetter.responseDtoSetter(HttpStatus.OK,"Current User Cart", cartRepository.findByUser(user));
    }
}
