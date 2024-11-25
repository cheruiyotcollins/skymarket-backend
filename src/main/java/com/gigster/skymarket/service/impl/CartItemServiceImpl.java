package com.gigster.skymarket.service.impl;

import com.gigster.skymarket.dto.CartItemDto;
import com.gigster.skymarket.dto.ResponseDto;
import com.gigster.skymarket.model.Cart;
import com.gigster.skymarket.model.CartItem;
import com.gigster.skymarket.model.Product;
import com.gigster.skymarket.repository.CartItemRepository;
import com.gigster.skymarket.repository.CartRepository;
import com.gigster.skymarket.repository.ProductRepository;
import com.gigster.skymarket.service.CartItemService;
import com.gigster.skymarket.mapper.ResponseDtoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CartItemServiceImpl implements CartItemService {
    @Autowired
    CartItemRepository cartItemRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    ResponseDtoMapper responseDtoSetter;

    @Autowired
    CartRepository cartRepository;

    @Override
    public ResponseEntity<ResponseDto> addOrUpdateCartItem(Optional<Cart> cart, Optional<Product> product, int quantity) {
        // Check if both cart and product are present
        if (cart.isPresent() && product.isPresent()) {
            Long cartId = cart.get().getId();
            Long productId = product.get().getId();

            Optional<CartItem> existingCartItem = cartItemRepository.findByCartIdAndProductId(cartId, productId);

            if (existingCartItem.isPresent()) {
                // Update the quantity of the existing cart item.
                CartItem cartItem = existingCartItem.get();
                cartItem.setQuantity(cartItem.getQuantity() + quantity);
                cartItemRepository.save(cartItem);

                ResponseDto response = ResponseDto.builder()
                        .status(HttpStatus.OK)
                        .description("Cart item updated successfully")
                        .payload(mapToDto(cartItem))
                        .build();

                return ResponseEntity.ok(response);
            } else {
                CartItem newCartItem = new CartItem();
                newCartItem.setCart(cart.get());
                newCartItem.setProduct(product.get());
                newCartItem.setQuantity(quantity);

                cartItemRepository.save(newCartItem);

                ResponseDto response = ResponseDto.builder()
                        .status(HttpStatus.CREATED)
                        .description("Cart item added successfully")
                        .payload(mapToDto(newCartItem))
                        .build();

                return ResponseEntity.status(HttpStatus.CREATED).body(response);
            }
        } else {
            String missingResource = cart.isEmpty() ? "Cart" : "Product";

            ResponseDto response = ResponseDto.builder()
                    .status(HttpStatus.NOT_FOUND)
                    .description(missingResource + " not found")
                    .payload(null)
                    .build();

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @Override
    public ResponseEntity<ResponseDto> getCartItem(Long cartId, Long itemId) {
        Optional<CartItem> cartItemOptional = cartItemRepository.findByCartIdAndProductId(cartId, itemId);

        if (cartItemOptional.isPresent()) {
            CartItemDto cartItemDto = mapToDto(cartItemOptional.get());

            ResponseDto response = ResponseDto.builder()
                    .status(HttpStatus.OK)
                    .description("Item retrieved successfully")
                    .payload(cartItemDto)
                    .build();

            return ResponseEntity.ok(response);
        } else {
            ResponseDto response = ResponseDto.builder()
                    .status(HttpStatus.NOT_FOUND)
                    .description("Item not found in cart.")
                    .payload(null)
                    .build();

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    //TODO: To check later if this is the better pagination logic or the other one.
    @Override
    public ResponseEntity<ResponseDto> getAllCartItems(Long cartId, Pageable pageable) {
        Page<CartItem> cartItemsPage = cartItemRepository.findByCartId(cartId, pageable);
        List<CartItemDto> cartItemDtos = cartItemsPage.getContent()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());

        ResponseDto responseDto = ResponseDto.builder()
                .status(HttpStatus.OK)
                .description("List of All Cart Items.")
                .payload(cartItemDtos)
                .totalPages(cartItemsPage.getTotalPages())
                .totalElements(cartItemsPage.getTotalElements())
                .currentPage(cartItemsPage.getNumber())
                .pageSize(cartItemsPage.getSize())
                .build();

        return ResponseEntity.ok(responseDto);
}

    @Override
    public ResponseEntity<ResponseDto> updateCartItem(Long cartId, Long itemId, int quantity) {
        Optional<CartItem> cartItemOptional = cartItemRepository.findByCartIdAndProductId(cartId, itemId);

        if (cartItemOptional.isPresent()) {
            CartItem cartItem = cartItemOptional.get();
            cartItem.setQuantity(quantity);
            cartItemRepository.save(cartItem);

            CartItemDto cartItemDto = mapToDto(cartItem);
            ResponseDto response = ResponseDto.builder()
                    .status(HttpStatus.OK)
                    .description("Cart item updated successfully.")
                    .payload(cartItemDto)
                    .build();

            return ResponseEntity.ok(response);
        } else {
            ResponseDto response = ResponseDto.builder()
                    .status(HttpStatus.NOT_FOUND)
                    .description("Item not found in cart.")
                    .payload(null)
                    .build();

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    // Method to map CartItem to CartItemDto.
    private CartItemDto mapToDto(CartItem cartItem) {
        return CartItemDto.builder()
                .productId(cartItem.getProduct().getId())
                .cartId(cartItem.getCart().getId())
                .quantity(cartItem.getQuantity())
                .subtotal(cartItem.getSubtotal())
                .build();
    }

}
