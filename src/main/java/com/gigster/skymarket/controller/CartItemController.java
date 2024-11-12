package com.gigster.skymarket.controller;

import com.gigster.skymarket.dto.CartItemRequestDto;
import com.gigster.skymarket.dto.ResponseDto;
import com.gigster.skymarket.model.Cart;
import com.gigster.skymarket.model.Product;
import com.gigster.skymarket.repository.CartRepository;
import com.gigster.skymarket.repository.ProductRepository;
import com.gigster.skymarket.service.CartItemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping(value = "/api/carts-items")
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

        return cartItemService.addOrUpdateCartItem(cart, product, request.getQuantity());
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
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sort", defaultValue = "id,asc") String sort) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort.split(",")));
        return cartItemService.getAllCartItems(cartId, pageable);
    }

    @PutMapping("/{cartId}/{itemId}")
    public ResponseEntity<ResponseDto> updateCartItem(
            @PathVariable Long cartId,
            @PathVariable Long itemId,
            @RequestParam int quantity) {
        return cartItemService.updateCartItem(cartId, itemId, quantity);
    }
}
