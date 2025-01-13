package com.gigster.skymarket.mapper;

import com.gigster.skymarket.dto.CartItemResponseDto;
import com.gigster.skymarket.model.CartItem;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class CartItemsToCartItemsDtoMapper {
    public HashMap<Double, List<CartItemResponseDto>> mapCartItemsToCartItemsDto(List<CartItem> cartItems) {
        HashMap<Double, List<CartItemResponseDto>> cartItemsHashmap = new HashMap<>();
        List<CartItemResponseDto> cartItemDtoList = new ArrayList<>();
        double totalPrice = 0.0;
        for (CartItem item : cartItems) {
            CartItemResponseDto cartItemDto = CartItemResponseDto.builder()
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
