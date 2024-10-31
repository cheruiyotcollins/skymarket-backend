package com.gigster.skymarket.mapper;

import com.gigster.skymarket.dto.CartItemDtoResponse;
import com.gigster.skymarket.model.CartItem;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class CartItemsToCartItemsDtoMapper {
    public HashMap<Double, List<CartItemDtoResponse>> mapCartItemsToCartItemsDto(List<CartItem> cartItems) {
        HashMap<Double, List<CartItemDtoResponse>> cartItemsHashmap = new HashMap<>();
        List<CartItemDtoResponse> cartItemDtoList = new ArrayList<>();
        double totalPrice = 0.0;
        for (CartItem item : cartItems) {
            CartItemDtoResponse cartItemDto = CartItemDtoResponse.builder()
                    .productName(item.getProduct().getProductName())
                    .price(item.getProduct().getPrice())
                    .quantity(item.getQuantity())
                    .subTotal(item.getSubtotal())
                    .build();
            cartItemDtoList.add(cartItemDto);
            totalPrice += item.getSubtotal();
        }

        cartItemsHashmap.put(totalPrice, cartItemDtoList);
        return cartItemsHashmap;
    }

}
