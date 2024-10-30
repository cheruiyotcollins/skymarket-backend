package com.gigster.skymarket.controller;

import com.gigster.skymarket.dto.CartDto;
import com.gigster.skymarket.dto.ResponseDto;
import com.gigster.skymarket.model.Role;
import com.gigster.skymarket.model.User;
import com.gigster.skymarket.security.CurrentUserV2;
import com.gigster.skymarket.security.UserPrincipal;
import com.gigster.skymarket.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/carts")
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

}
