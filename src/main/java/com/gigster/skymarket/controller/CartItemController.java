package com.gigster.skymarket.controller;

import com.gigster.skymarket.dto.CartItemRequestDto;
import com.gigster.skymarket.dto.ResponseDto;
import com.gigster.skymarket.model.Cart;
import com.gigster.skymarket.model.Product;
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
    private ProductRepository productRepository;

    @PostMapping
    public ResponseEntity<ResponseDto> addOrUpdateCartItem(@RequestBody CartItemRequestDto request) {
        Optional<Cart> cart = cartRepository.findById(request.getCartId());
        Optional<Product> product = productRepository.findById(request.getProductId());

        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return cartItemService.addOrUpdateCartItem(cart, product, request.getQuantity(), userPrincipal);
    }

    @GetMapping("/{cartId}/{itemId}")
    public ResponseEntity<ResponseDto> getCartItem(
            @PathVariable Long cartId,
            @PathVariable Long itemId) {
        return cartItemService.getCartItem(cartId, itemId);
    }

    @GetMapping("/{cartId}")
    public ResponseEntity<ResponseDto> getAllCartItems(
            @PathVariable("cartId") Long cartId,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "20") int size,
            @RequestParam(value = "sort", defaultValue = "id,asc") String sort) {

        return cartItemService.getAllCartItems(cartId, page, size, sort);
    }

    @PutMapping("/{cartId}/{itemId}")
    public ResponseEntity<ResponseDto> updateCartItem(
            @PathVariable Long cartId,
            @PathVariable Long itemId,
            @RequestParam int quantity) {
        return cartItemService.updateCartItem(cartId, itemId, quantity);
    }
}
