package com.gigster.skymarket.controller;

import com.gigster.skymarket.dto.CartDto;
import com.gigster.skymarket.dto.ResponseDto;
import com.gigster.skymarket.security.CurrentUserV2;
import com.gigster.skymarket.security.UserPrincipal;
import com.gigster.skymarket.service.CartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/carts")
@Slf4j
public class CartController {

    @Autowired
    CartService cartService;

    @PostMapping
    public ResponseEntity<ResponseDto> addCart(){
        UserPrincipal userPrincipal= CurrentUserV2.getCurrentUser();
       CartDto cartDto= new CartDto();
       cartDto.setCustomer(CurrentUserV2.mapToCustomer(userPrincipal));
        return cartService.addCart(cartDto);
    }

    @GetMapping
    public ResponseEntity<ResponseDto> getAllCarts(Pageable pageable) {
        return cartService.getAllCarts(pageable);
    }

    @GetMapping("/customerId")
    public ResponseEntity<ResponseDto> findCartByCustomerId(Authentication authentication){
        String username=authentication.getName();
        return cartService.findCartPerCustomer(username);
    }

    @DeleteMapping("/remove/{productId}")
    public ResponseEntity<ResponseDto> removeItemFromCart(@PathVariable Long productId, Authentication authentication) {
        UserPrincipal userPrincipal = CurrentUserV2.getCurrentUser();
        return ResponseEntity.ok(cartService.removeItemFromCart(productId).getBody());
    }

    @DeleteMapping("/clear/{customerId}")
    public ResponseEntity<ResponseDto> clearCart(@PathVariable Long customerId) {
        return cartService.clearCart(customerId);
    }

}



