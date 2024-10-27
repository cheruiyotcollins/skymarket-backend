package com.gigster.skymarket.service.impl;

import com.gigster.skymarket.dto.CartItemDto;
import com.gigster.skymarket.dto.ResponseDto;
import com.gigster.skymarket.model.Cart;
import com.gigster.skymarket.model.CartItem;
import com.gigster.skymarket.repository.CartItemRepository;
import com.gigster.skymarket.repository.ProductRepository;
import com.gigster.skymarket.service.CartItemService;
import com.gigster.skymarket.setter.ResponseDtoSetter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartItemServiceImpl implements CartItemService {
    @Autowired
    CartItemRepository cartItemRepository;

    @Autowired
    ProductRepository productRepository;
    @Autowired
    ResponseDtoSetter responseDtoSetter;


    @Override
    public ResponseEntity<ResponseDto> addItemToCart(Cart cart, CartItemDto cartItemDto) {

        try{
            CartItem cartItem= new CartItem();
            cartItem.setCart(cart);
            //todo avoid using get without is present
            cartItem.setProduct(productRepository.findById(cartItemDto.getProductId()).get());
            cartItem.setQuantity(cartItemDto.getQuantity());
            cartItemRepository.save(cartItem);
            return responseDtoSetter.responseDtoSetter(HttpStatus.CREATED,"Item added successfully", cartItemRepository.save(cartItem));
        } catch (Exception e) {
            return responseDtoSetter.responseDtoSetter(HttpStatus.BAD_REQUEST, "something went wrong please check your request: "+ e, new Object());
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
