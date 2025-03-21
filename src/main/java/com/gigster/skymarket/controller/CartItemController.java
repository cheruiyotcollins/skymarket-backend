package com.gigster.skymarket.controller;

import com.gigster.skymarket.dto.CartItemRequestDto;
import com.gigster.skymarket.dto.ResponseDto;
import com.gigster.skymarket.model.Cart;
import com.gigster.skymarket.model.CartItem;
import com.gigster.skymarket.model.Product;
import com.gigster.skymarket.repository.CartItemRepository;
import com.gigster.skymarket.repository.CartRepository;
import com.gigster.skymarket.repository.ProductRepository;
import com.gigster.skymarket.security.UserPrincipal;
import com.gigster.skymarket.service.CartItemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping(value = "/api/v1/carts-items")
@Slf4j
public class CartItemController {

    @Autowired
    private CartItemService cartItemService;

    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @PostMapping
    public ResponseEntity<ResponseDto> addCartItem(@RequestBody CartItemRequestDto request) {
        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<Cart> cart;
        if(request.getCartId()==null){
            cart= cartRepository.findByCustomerId(userPrincipal.getCustomerId());
        }else {
            cart = cartRepository.findById(request.getCartId());
        }
        Optional<Product> product = productRepository.findById(request.getProductId());

        return cartItemService.addCartItem(cart, product, request.getQuantity(), userPrincipal);
    }

    @PatchMapping
    public ResponseEntity<ResponseDto> updateCartItem(@RequestBody CartItemRequestDto request) {
        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<Cart> cart;
        if(request.getCartId()==null){
             cart= cartRepository.findByCustomerId(userPrincipal.getCustomerId());
        }else {
             cart = cartRepository.findById(request.getCartId());
        }

        Optional<CartItem> cartItem = cartItemRepository.findByCart_CartIdAndId(request.getCartId(), request.getCartItemId());

        return cartItemService.updateCartItem(cart, cartItem, request.getQuantity(), userPrincipal);
    }

    @GetMapping("/{cartId}/{itemId}")
    public ResponseEntity<ResponseDto> getCartItem(
            @PathVariable Long cartId,
            @PathVariable Long itemId) {
        return cartItemService.getCartItem(cartId, itemId);
    }

//    @GetMapping("/{cartId}")
//    public ResponseEntity<ResponseDto> getAllCartItems(
//            @PathVariable("cartId") Long cartId,
//            @RequestParam(value = "page", defaultValue = "0") int page,
//            @RequestParam(value = "size", defaultValue = "20") int size,
//            @RequestParam(value = "sort", defaultValue = "id,asc") String sort) {
//
//        return cartItemService.getAllCartItems(cartId, page, size, sort);
//    }

    @PutMapping("/{cartId}/{itemId}")
    public ResponseEntity<ResponseDto> updateCartItem(
            @PathVariable Long cartId,
            @PathVariable Long itemId,
            @RequestParam int quantity) {
        return cartItemService.updateCartItem(cartId, itemId, quantity);
    }
}