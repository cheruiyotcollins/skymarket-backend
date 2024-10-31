package com.gigster.skymarket.service.impl;

import com.gigster.skymarket.dto.CartItemDto;
import com.gigster.skymarket.dto.ResponseDto;
import com.gigster.skymarket.model.Cart;
import com.gigster.skymarket.model.CartItem;
import com.gigster.skymarket.model.Product;
import com.gigster.skymarket.repository.CartItemRepository;
import com.gigster.skymarket.repository.CartRepository;
import com.gigster.skymarket.repository.ProductRepository;
import com.gigster.skymarket.service.CartItemService;
import com.gigster.skymarket.mapper.ResponseDtoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CartItemServiceImpl implements CartItemService {
    @Autowired
    CartItemRepository cartItemRepository;

    @Autowired
    ProductRepository productRepository;
    @Autowired
    ResponseDtoMapper responseDtoSetter;
    @Autowired
    CartRepository cartRepository;


    @Override
    public ResponseEntity<ResponseDto> addItemToCart(CartItemDto cartItemDto) {
        try {
            Optional<Cart> cart = cartRepository.findById(cartItemDto.getCartId());
            Optional<Product> product = productRepository.findById(cartItemDto.getProductId());

            if (cart.isEmpty() || product.isEmpty()) {
                return responseDtoSetter.responseDtoSetter(HttpStatus.BAD_REQUEST, "Invalid cart or product ID.");
            }

            // Check if the product already exists in the cart
            Optional<CartItem> existingCartItem = cartItemRepository.findByCartAndProduct(cart.get(), product.get());

            if (existingCartItem.isPresent()) {
                // Update quantity of existing item
                CartItem cartItem = existingCartItem.get();
                cartItem.setQuantity(cartItem.getQuantity() + cartItemDto.getQuantity());
                cartItemRepository.save(cartItem);
            } else {
                // Create new cart item
                CartItem cartItem = new CartItem();
                cartItem.setCart(cart.get());
                cartItem.setProduct(product.get());
                cartItem.setQuantity(cartItemDto.getQuantity());
                cartItemRepository.save(cartItem);
            }

            return responseDtoSetter.responseDtoSetter(HttpStatus.CREATED, "Item added successfully");
        } catch (Exception e) {
            return responseDtoSetter.responseDtoSetter(HttpStatus.BAD_REQUEST, "Something went wrong. Please check your request: " + e, new Object());
        }
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
