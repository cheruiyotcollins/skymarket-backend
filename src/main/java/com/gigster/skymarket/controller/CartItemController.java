package com.gigster.skymarket.controller;

import com.gigster.skymarket.dto.CartItemDto;
import com.gigster.skymarket.dto.ResponseDto;
import com.gigster.skymarket.service.CartItemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/carts/items")
@Slf4j
public class CartItemController {
    @Autowired
    CartItemService cartItemService;
   @PostMapping
    public ResponseEntity<ResponseDto> addCartITem(@RequestBody CartItemDto cartItemDto){
       return cartItemService.addItemToCart(cartItemDto);
    }
    @GetMapping("/{id}")
    public ResponseEntity<List<CartItemDto>> findAll(@PathVariable long id){
        return cartItemService.getCartItems(id);
    }
}

