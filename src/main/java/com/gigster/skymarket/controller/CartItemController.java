package com.gigster.skymarket.controller;

import com.gigster.skymarket.dto.CartItemDto;
import com.gigster.skymarket.dto.ResponseDto;
import com.gigster.skymarket.model.Cart;
import com.gigster.skymarket.repository.CartRepository;
import com.gigster.skymarket.service.CartItemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/api/cart/items")
@Slf4j
public class CartItemController {
    @Autowired
    CartItemService cartItemService;
    //todo remove this
    @Autowired
    CartRepository cartRepository;
   @PostMapping
    public ResponseEntity<ResponseDto> addCartITem(@RequestBody CartItemDto cartItemDto, long cartId){
        Optional<Cart>  cart= cartRepository.findById(cartId);
       cart.ifPresent(value -> cartItemService.addItemToCart(value, cartItemDto));
       return null;
    }
    @GetMapping("/{id}")
    public ResponseEntity<List<CartItemDto>> findAll(@PathVariable long id){
        return cartItemService.getCartItems(id);
    }
}

