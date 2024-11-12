package com.gigster.skymarket.service;

import com.gigster.skymarket.dto.ResponseDto;
import com.gigster.skymarket.model.Cart;
import com.gigster.skymarket.model.Product;
import org.springframework.http.ResponseEntity;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface CartItemService {
    ResponseEntity<ResponseDto> addOrUpdateCartItem(Optional<Cart> cart, Optional<Product> product, int quantity);

    ResponseEntity<ResponseDto> getCartItem(Long cartId, Long itemId);

    //TODO: To check later if this is the better pagination logic or the other one.
    ResponseEntity<ResponseDto> getAllCartItems(Long cartId, Pageable pageable);

    ResponseEntity<ResponseDto> updateCartItem(Long cartId, Long itemId, int quantity);
}
