package com.gigster.skymarket.service.impl;


import com.gigster.skymarket.dto.*;
import com.gigster.skymarket.mapper.CartMapper;
import com.gigster.skymarket.model.Cart;
import com.gigster.skymarket.model.User;
import com.gigster.skymarket.repository.CartItemRepository;
import com.gigster.skymarket.repository.CartRepository;
import com.gigster.skymarket.repository.UserRepository;
import com.gigster.skymarket.service.CartService;
import com.gigster.skymarket.mapper.CartItemsToCartItemsDtoMapper;
import com.gigster.skymarket.mapper.ResponseDtoMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class CartServiceImpl implements CartService {
    @Autowired
    CartRepository cartRepository;

    @Autowired
    CartItemRepository cartItemRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ResponseDtoMapper responseDtoSetter;

    @Autowired
    CartMapper cartMapper;

    @Autowired
    CartItemsToCartItemsDtoMapper cartItemsToCartItemsDtoMapper;

    @Override
    public ResponseEntity<ResponseDto> addCart(CartDto cartDto) {
            Cart cart= new Cart();
            cart.setUser(cartDto.getUser());
        cartRepository.save(cart);
            return responseDtoSetter.responseDtoSetter(HttpStatus.CREATED,"Cart Added Successfully", cart);

    }
    @Override
    public ResponseEntity<ResponseDto> getAllCarts(Pageable pageable) {
        Page<Cart> cartPage = cartRepository.findAll(pageable);

        List<CartDto> cartDtos = cartPage.getContent()
                .stream()
                .map(cartMapper::toDto)
                .toList();

        ResponseDto responseDto = responseDtoSetter.responseDtoSetter(
                HttpStatus.OK,
                "Fetched List of All Carts.",
                cartDtos
        ).getBody();

        assert responseDto != null;
        responseDto.setTotalPages(cartPage.getTotalPages());
        responseDto.setTotalElements(cartPage.getTotalElements());
        responseDto.setCurrentPage(pageable.getPageNumber());
        responseDto.setPageSize(pageable.getPageSize());

        return ResponseEntity.ok(responseDto);
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
    public ResponseEntity<ResponseDto> findCartPerUser(String email) {
        User user = userRepository.findByUsername(email).get();
        Cart cart = cartRepository.findByUser(user).get();
        // Map the cart items to a hashmap with total price and item list
        HashMap<Double, List<CartItemDtoResponse>> cartItemsHashMap = cartItemsToCartItemsDtoMapper.mapCartItemsToCartItemsDto(cart.getCartItems());
        // Since we have only one entry, retrieve it
        Map.Entry<Double, List<CartItemDtoResponse>> entry = cartItemsHashMap.entrySet().iterator().next();
        double totalPrice = entry.getKey();
        List<CartItemDtoResponse> cartItemDtoList = entry.getValue();

        // Create response DTO
        CartDtoResponse cartDtoResponse = CartDtoResponse.builder()
                .name(user.getFullName())
                .cartItemDtoList(cartItemDtoList)
                .totalPrice(totalPrice)  // Set actual total price here
                .build();

        return responseDtoSetter.responseDtoSetter(HttpStatus.OK, "Current User Cart", cartDtoResponse);
    }
}
