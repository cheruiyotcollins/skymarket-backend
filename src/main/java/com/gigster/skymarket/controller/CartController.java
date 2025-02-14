package com.gigster.skymarket.controller;

import com.gigster.skymarket.dto.ResponseDto;
import com.gigster.skymarket.security.UserPrincipal;
import com.gigster.skymarket.service.CartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/carts")
@Slf4j
public class CartController {

    @Autowired
    private CartService cartService;

    private UserPrincipal getCurrentUser() {
        return (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    @PostMapping
    public ResponseEntity<ResponseDto> createCart() {
        ResponseDto response = cartService.createCart(getCurrentUser()).getBody();

        log.info("Cart operation completed for customer ID: {}", getCurrentUser().getCustomerId());

        assert response != null;
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping
    public ResponseEntity<ResponseDto> getAllCarts(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sort", defaultValue = "id,asc") String sort) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort.split(",")));
        return cartService.getAllCarts(pageable, getCurrentUser());
    }

    @GetMapping("/customer")
    public ResponseEntity<ResponseDto> findCartByCustomerId(Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        String email = userPrincipal.getEmail();
        log.info("Fetching cart for customer with email: {}", email);
        return cartService.findCartPerCustomer(email);
    }

    @DeleteMapping("/remove/{productId}")
    public ResponseEntity<ResponseDto> removeItemFromCart(@PathVariable Long productId) {
        UserPrincipal userPrincipal = getCurrentUser();
        log.info("Authenticated UserPrincipal for removal: {}", userPrincipal);
        log.info("Removing product with ID: {}", productId);

        ResponseEntity<ResponseDto> response = cartService.removeItemFromCart(productId);
        log.info("Response after removal: {}", response.getBody());
        return response;
    }

    @DeleteMapping("/clear/{customerId}")
    public ResponseEntity<ResponseDto> clearCart(@PathVariable Long customerId) {
        log.info("Clearing cart for customer with ID: {}", customerId);

        ResponseEntity<ResponseDto> response = cartService.clearCart(customerId);
        log.info("Response after clearing cart: {}", response.getBody());
        return response;
    }
}
