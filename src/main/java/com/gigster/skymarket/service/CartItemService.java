package com.gigster.skymarket.service;

import com.gigster.skymarket.dto.ResponseDto;
import com.gigster.skymarket.model.Cart;
import com.gigster.skymarket.model.Product;
import com.gigster.skymarket.security.UserPrincipal;
import org.springframework.http.ResponseEntity;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface CartItemService {

    ResponseEntity<ResponseDto> addOrUpdateCartItem(Optional<Cart> cart, Optional<Product> product, int quantity, UserPrincipal userPrincipal);

    ResponseEntity<ResponseDto> getCartItem(Long cartId, Long itemId);

    ResponseEntity<ResponseDto> getAllCartItems(Long cartId, Pageable pageable);

    ResponseEntity<ResponseDto> updateCartItem(Long cartId, Long itemId, int quantity);
}