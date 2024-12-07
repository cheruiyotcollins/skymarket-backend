package com.gigster.skymarket.service.impl;

import com.gigster.skymarket.dto.*;
import com.gigster.skymarket.mapper.CartMapper;
import com.gigster.skymarket.model.Cart;
import com.gigster.skymarket.model.CartItem;
import com.gigster.skymarket.model.Customer;
import com.gigster.skymarket.model.User;
import com.gigster.skymarket.repository.CartItemRepository;
import com.gigster.skymarket.repository.CartRepository;
import com.gigster.skymarket.repository.CustomerRepository;
import com.gigster.skymarket.repository.UserRepository;
import com.gigster.skymarket.service.CartService;
import com.gigster.skymarket.mapper.CartItemsToCartItemsDtoMapper;
import com.gigster.skymarket.mapper.ResponseDtoMapper;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class CartServiceImpl implements CartService {
    @Autowired
    CartRepository cartRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CartItemRepository cartItemRepository;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    ResponseDtoMapper responseDtoSetter;

    @Autowired
    CartMapper cartMapper;

    @Autowired
    CartItemsToCartItemsDtoMapper cartItemsToCartItemsDtoMapper;

    @Override
    public ResponseEntity<ResponseDto> addCart(Long customerId) {
        log.info("Creating cart for customer ID: {}", customerId);

        try {
            Customer customer = customerRepository.findById(customerId)
                    .orElseThrow(() -> new RuntimeException("Customer not found with ID: " + customerId));

            // If customer exists, check if the user table is also updated correctly
            if (userRepository.existsByCustomerId(customerId)) {
                log.warn("Customer ID {} already has a user record.", customerId);
            } else {
                log.info("Customer ID {} does not have a corresponding user record.", customerId);
                // Add the user record as needed
                User user = new User();
                user.setCustomerId(customer.getCustomerId());
                userRepository.save(user);
            }

            // Check if the customer already has a cart
            if (cartRepository.existsByCustomerId(customerId)) {
                log.warn("Customer ID {} already has a cart.", customerId);
                return ResponseEntity.status(HttpStatus.CONFLICT).body(
                        ResponseDto.builder()
                                .status(HttpStatus.CONFLICT)
                                .description("Customer already has a cart.")
                                .build()
                );
            }

            // Proceed to create the cart
            Cart cart = new Cart();
            cart.setCustomer(customer);
            cart.setTotalPrice(0.0);
            cartRepository.save(cart);

            log.info("Cart created successfully for customer ID: {}", customer.getCustomerId());
            return ResponseEntity.status(HttpStatus.CREATED).body(
                    ResponseDto.builder()
                            .status(HttpStatus.CREATED)
                            .description("Cart added successfully.")
                            .payload(cart)
                            .build()
            );

        } catch (RuntimeException e) {
            log.error("Failed to create cart for customer ID {}: {}", customerId, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ResponseDto.builder()
                            .status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .description("Failed to create cart: " + e.getMessage())
                            .build()
            );
        }
    }

    @Override
    public ResponseEntity<ResponseDto> getAllCarts(Pageable pageable) {
        Page<Cart> cartPage = cartRepository.findAll(pageable);

        List<CartDto> cartDtos = cartPage.getContent()
                .stream()
                .map(cartMapper::toDto)
                .toList();

        ResponseDto responseDto = responseDtoSetter.responseDtoSetter(
                HttpStatus.OK,
                "Fetched List of All Carts.",
                cartDtos
        ).getBody();

        assert responseDto != null;
        responseDto.setTotalPages(cartPage.getTotalPages());
        responseDto.setTotalElements(cartPage.getTotalElements());
        responseDto.setCurrentPage(pageable.getPageNumber());
        responseDto.setPageSize(pageable.getPageSize());

        return ResponseEntity.ok(responseDto);
    }

    @Override
    @Transactional
    public ResponseEntity<ResponseDto> removeItemFromCart(Long productId) {
        try {
            // Fetch the cart item by productId
            CartItem cartItem = cartItemRepository.findByProductId(productId)
                    .orElseThrow(() -> new RuntimeException("Cart item not found for product ID: " + productId));

            // Fetch the associated cart and remove the item
            Cart cart = cartItem.getCart();
            cart.getCartItems().remove(cartItem);

            // Delete the cart item from the repository
            cartItemRepository.delete(cartItem);

            // Recalculate and update the total price
            double newTotalPrice = cart.getCartItems().stream()
                    .mapToDouble(CartItem::getSubtotal)
                    .sum();
            cart.setTotalPrice(newTotalPrice);

            // Save the updated cart if not empty, else delete the cart
            if (cart.getCartItems().isEmpty()) {
                cartRepository.delete(cart);
                log.info("Cart is empty. Cart with ID {} has been deleted.", cart.getId());
            } else {
                cartRepository.save(cart);
            }

            // Log and build success response
            log.info("Cart item with product ID {} removed successfully. New total price: {}", productId, newTotalPrice);
            ResponseDto response = ResponseDto.builder()
                    .status(HttpStatus.OK)
                    .description("Product removed from cart successfully.")
                    .build();

            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            // Handle errors and build failure response
            log.error("Failed to remove product from cart: {}", e.getMessage());
            ResponseDto response = ResponseDto.builder()
                    .status(HttpStatus.NOT_FOUND)
                    .description("Failed to remove product from cart: " + e.getMessage())
                    .build();

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @Override
    @Transactional
    public ResponseEntity<ResponseDto> clearCart(Long customerId) {
        try {
            // Fetch the cart for the given customer
            Cart cart = cartRepository.findByCustomerId(customerId)
                    .orElseThrow(() -> new RuntimeException("Cart not found for customer ID: " + customerId));

            // Check if the cart is already empty
            if (cart.getCartItems().isEmpty()) {
                return ResponseEntity.ok(
                        ResponseDto.builder()
                                .status(HttpStatus.OK)
                                .description("Cart is already empty.")
                                .payload(null)
                                .build()
                );
            }

            // Clear all items from the cart
            cartItemRepository.deleteAll(cart.getCartItems());
            cart.getCartItems().clear();

            // Set total price to zero and save the updated cart
            cart.setTotalPrice(0.0);
            cartRepository.save(cart);

            // Return success response
            return ResponseEntity.ok(
                    ResponseDto.builder()
                            .status(HttpStatus.OK)
                            .description("All items removed from cart successfully.")
                            .payload(null)
                            .build()
            );

        } catch (RuntimeException e) {
            // Handle errors (e.g., cart not found)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    ResponseDto.builder()
                            .status(HttpStatus.NOT_FOUND)
                            .description("Failed to clear cart: " + e.getMessage())
                            .payload(null)
                            .build()
            );
        }
    }

    @Override
    public double calculateTotalPrice() {
        return 0;
    }

    @Override
    public ResponseEntity<ResponseDto> findCartPerCustomer(String email) {
        // Fetch the customer using the email
        Customer customer = customerRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Customer not found!"));

        // Fetch the cart associated with the customer
        Cart cart = cartRepository.findByCustomerId(customer.getCustomerId()).orElseThrow(() -> new RuntimeException("Cart not found!"));

        // Map the cart items to a hashmap with total price and item list
        HashMap<Double, List<CartItemDtoResponse>> cartItemsHashMap = cartItemsToCartItemsDtoMapper.mapCartItemsToCartItemsDto(cart.getCartItems());

        Map.Entry<Double, List<CartItemDtoResponse>> entry = cartItemsHashMap.entrySet().iterator().next();
        double totalPrice = entry.getKey();
        List<CartItemDtoResponse> cartItemDtoList = entry.getValue();

        CartDtoResponse cartDtoResponse = CartDtoResponse.builder()
                .name(customer.getFullName())
                .cartItemDtoList(cartItemDtoList)
                .totalPrice(totalPrice)
                .build();

        return responseDtoSetter.responseDtoSetter(HttpStatus.OK, "Current Customer Cart", cartDtoResponse);
    }

}