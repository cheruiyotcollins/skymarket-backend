package com.gigster.skymarket.service.impl;

import com.gigster.skymarket.dto.CartItemDto;
import com.gigster.skymarket.dto.ResponseDto;
import com.gigster.skymarket.model.Cart;
import com.gigster.skymarket.model.CartItem;
import com.gigster.skymarket.model.Customer;
import com.gigster.skymarket.model.Product;
import com.gigster.skymarket.repository.CartItemRepository;
import com.gigster.skymarket.repository.CartRepository;
import com.gigster.skymarket.repository.CustomerRepository;
import com.gigster.skymarket.repository.ProductRepository;
import com.gigster.skymarket.security.CurrentUserV2;
import com.gigster.skymarket.security.UserPrincipal;
import com.gigster.skymarket.service.CartItemService;
import com.gigster.skymarket.mapper.ResponseDtoMapper;
import com.gigster.skymarket.service.CartService;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CartItemServiceImpl implements CartItemService {

    private final CartService cartService;
    private final CustomerRepository customerRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final ResponseDtoMapper responseDtoSetter;

    @Override
    public ResponseEntity<ResponseDto> addCartItem(Optional<Cart> cart, Optional<Product> product, int quantity, UserPrincipal userPrincipal) {
        try {
            // Map the authenticated user to a customer
            Customer customer = CurrentUserV2.mapToCustomer(userPrincipal);
            log.debug("Mapped customer: {}", customer);

            // Validate customer existence
            Optional<Customer> existingCustomer = customerRepository.findById(customer.getCustomerId());
            if (existingCustomer.isEmpty()) {
                log.error("Customer with ID {} does not exist.", customer.getCustomerId());
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        ResponseDto.builder()
                                .status(HttpStatus.NOT_FOUND)
                                .description("Customer not found.")
                                .build());
            }

            // If cart does not exist, create one using the existing method.
            Cart customerCart = cart.orElseGet(() ->
                    cartRepository.findByCustomerId(customer.getCustomerId())
                            .orElseGet(() -> {
                                ResponseEntity<ResponseDto> response = cartService.createCart(userPrincipal);
                                if (response.getStatusCode().is2xxSuccessful()) {
                                    return cartRepository.findByCustomerId(customer.getCustomerId()).orElseThrow();
                                } else {
                                    throw new RuntimeException("Failed to create cart.");
                                }
                            })
            );

            // Ensure product exists
            if (product.isEmpty()) {
                log.error("Product not found.");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        ResponseDto.builder()
                                .status(HttpStatus.NOT_FOUND)
                                .description("Product not found.")
                                .build());
            }

            Long cartId = customerCart.getCartId();
            Long productId = product.get().getId();

            Optional<CartItem> existingCartItem = cartItemRepository.findByCart_CartIdAndProductId(cartId, productId);

            if (existingCartItem.isPresent()) {
                // Update the quantity of the existing cart item.
                CartItem cartItem = existingCartItem.get();
                cartItem.setQuantity(cartItem.getQuantity() + quantity);
                cartItemRepository.save(cartItem);

                log.info("Cart item updated successfully for cart ID {}", cartId);
                return ResponseEntity.ok(
                        ResponseDto.builder()
                                .status(HttpStatus.OK)
                                .description("Cart item updated successfully.")
                                .build());
            } else {
                // Add a new item to the cart.
                CartItem newCartItem = new CartItem();
                newCartItem.setCart(customerCart);
                newCartItem.setProduct(product.get());
                newCartItem.setQuantity(quantity);
                cartItemRepository.save(newCartItem);

                log.info("Cart item added successfully for cart ID {}", cartId);
                return ResponseEntity.status(HttpStatus.CREATED).body(
                        ResponseDto.builder()
                                .status(HttpStatus.CREATED)
                                .description("Cart item added successfully.")
                                .build());
            }
        } catch (Exception e) {
            log.error("Error processing cart item: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ResponseDto.builder()
                            .status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .description("Failed to process cart item: " + e.getMessage())
                            .build());
        }
    }

    @Override
    public ResponseEntity<ResponseDto> updateCartItem(Optional<Cart> cart, Optional<CartItem> cartItem, int quantity, UserPrincipal userPrincipal) {
        try {
            // Map the authenticated user to a customer
            Customer customer = CurrentUserV2.mapToCustomer(userPrincipal);
            log.debug("Mapped customer: {}", customer);

            // Validate customer existence
            Optional<Customer> existingCustomer = customerRepository.findById(customer.getCustomerId());
            if (existingCustomer.isEmpty()) {
                log.error("Customer with ID {} does not exist.", customer.getCustomerId());
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        ResponseDto.builder()
                                .status(HttpStatus.NOT_FOUND)
                                .description("Customer not found.")
                                .build());
            }

            // Validate cart existence
            if (cart.isEmpty()) {
                log.error("Cart with ID {} not found.", customer.getCustomerId());
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        ResponseDto.builder()
                                .status(HttpStatus.NOT_FOUND)
                                .description("Cart not found.")
                                .build());
            }

            // Validate cart item existence
            if (cartItem.isEmpty()) {
                log.error("Cart item with ID not found in cart.");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        ResponseDto.builder()
                                .status(HttpStatus.NOT_FOUND)
                                .description("Cart item not found.")
                                .build());
            }

            Cart existingCart = cart.get();
            CartItem existingCartItem = cartItem.get();

            // Ensure the cart belongs to the authenticated user
            if (!existingCart.getCustomer().getCustomerId().equals(customer.getCustomerId())) {
                log.error("User is not authorized to update this cart.");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                        ResponseDto.builder()
                                .status(HttpStatus.FORBIDDEN)
                                .description("You are not authorized to update this cart.")
                                .build());
            }

            // Update the quantity
            existingCartItem.setQuantity(existingCartItem.getQuantity()+quantity);
            cartItemRepository.save(existingCartItem);

            log.info("Cart item updated successfully.");
            return ResponseEntity.status(HttpStatus.OK).body(
                    ResponseDto.builder()
                            .status(HttpStatus.OK)
                            .description("Cart item updated successfully.")
                            .build());

        } catch (Exception e) {
            log.error("Error updating cart item: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ResponseDto.builder()
                            .status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .description("An error occurred while updating the cart item.")
                            .build());
        }
    }


    @Override
    public ResponseEntity<ResponseDto> getCartItem(Long cartId, Long itemId) {
        Optional<CartItem> cartItemOptional = cartItemRepository.findByCart_CartIdAndProductId(cartId, itemId);

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

    @Override
    public ResponseEntity<ResponseDto> getAllCartItems(Long cartId, Pageable pageable) {
        Page<CartItem> cartItemsPage = cartItemRepository.findByCart_CartId(cartId, pageable);
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
        Optional<CartItem> cartItemOptional = cartItemRepository.findByCart_CartIdAndProductId(cartId, itemId);

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
                .cartId(cartItem.getCart().getCartId())
                .quantity(cartItem.getQuantity())
                .subtotal(cartItem.getSubtotal())
                .build();
    }

}