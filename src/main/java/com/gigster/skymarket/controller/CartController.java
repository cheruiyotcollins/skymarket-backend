package com.gigster.skymarket.controller;

import com.gigster.skymarket.dto.CartDto;
import com.gigster.skymarket.dto.ResponseDto;
import com.gigster.skymarket.model.Role;
import com.gigster.skymarket.model.User;
import com.gigster.skymarket.security.CurrentUserV2;
import com.gigster.skymarket.security.UserPrincipal;
import com.gigster.skymarket.service.CartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

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
       cartDto.setUser(CurrentUserV2.mapToUser(userPrincipal));
        return cartService.addCart(cartDto);
    }
    @GetMapping
    public ResponseEntity<ResponseDto> findAllCarts(){
        return cartService.getAllCarts();
    }
    @GetMapping("/customer")
    public ResponseEntity<ResponseDto> findCartByCustomerId(Authentication authentication){
        String username=authentication.getName();
        return cartService.findCartPerUser(username);
    }

}
