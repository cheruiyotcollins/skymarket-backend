package com.gigster.skymarket.mapper;

import com.gigster.skymarket.dto.CartDto;
import com.gigster.skymarket.model.Cart;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CartMapper {

    CartDto toDto(Cart cart);

    Cart toEntity(CartDto cartDto);

    List<CartDto> toDtoList(List<Cart> carts);

    List<Cart> toEntityList(List<CartDto> cartDtos);
}

