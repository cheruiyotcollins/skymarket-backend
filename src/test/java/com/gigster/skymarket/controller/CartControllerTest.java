package com.gigster.skymarket.controller;

import com.gigster.skymarket.dto.ResponseDto;
import com.gigster.skymarket.enums.RoleName;
import com.gigster.skymarket.model.Role;
import com.gigster.skymarket.model.User;
import com.gigster.skymarket.security.CurrentUserV2;
import com.gigster.skymarket.security.UserPrincipal;
import com.gigster.skymarket.service.CartService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.Objects;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CartControllerTest {

    @Mock
    private CartService cartService;

    @Mock
    private Authentication authentication;

    @Mock
    private UserPrincipal userPrincipal;

    @InjectMocks
    private CartController cartController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddCart() {
        // Mock User object
        User mockUser = new User();
        mockUser.setUserId(1L);
        mockUser.setFullName("Test User");
        mockUser.setUsername("testUser");
        mockUser.setEmail("testuser@example.com");
        mockUser.setContact("123456789");
        mockUser.setPassword("password");
        mockUser.setRoles(Set.of(new Role(1L, RoleName.ROLE_CUSTOMER)));

        // Create UserPrincipal using UserPrincipal.create()
        UserPrincipal mockUserPrincipal = UserPrincipal.create(mockUser);

        // Mock CurrentUserV2
        when(CurrentUserV2.getCurrentUser()).thenReturn(mockUserPrincipal);

        // Mock service response
        ResponseDto responseDto = ResponseDto.builder()
                .status(HttpStatus.OK)
                .description("Cart added successfully")
                .payload(null)
                .build();
        when(cartService.addCart(mockUserPrincipal)).thenReturn(ResponseEntity.ok(responseDto));

        // Call the method
        ResponseEntity<ResponseDto> response = cartController.addCart();

        // Verify
        assertNotNull(response);
        assertEquals(HttpStatus.OK, Objects.requireNonNull(response.getBody()).getStatus());
        assertEquals("Cart added successfully.", response.getBody().getDescription());
        verify(cartService, times(1)).addCart(mockUserPrincipal);
    }

    @Test
    void testGetAllCarts() {
        // Mock service response
        ResponseDto responseDto = ResponseDto.builder()
                .status(HttpStatus.OK)
                .description("Fetched all carts")
                .payload(null)
                .totalPages(1)
                .totalElements(5L)
                .currentPage(0)
                .pageSize(10)
                .build();

        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());

        // Mock the UserPrincipal and Authentication
        User mockUser = new User();
        mockUser.setUserId(1L);
        mockUser.setFullName("Test User");
        mockUser.setUsername("testUser");
        UserPrincipal mockUserPrincipal = UserPrincipal.create(mockUser);

        Authentication mockAuthentication = mock(Authentication.class);
        when(mockAuthentication.getPrincipal()).thenReturn(mockUserPrincipal);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(mockAuthentication);
        SecurityContextHolder.setContext(securityContext);

        when(cartService.getAllCarts(pageable, mockUserPrincipal)).thenReturn(ResponseEntity.ok(responseDto));

        // Call the method
        ResponseEntity<ResponseDto> response = cartController.getAllCarts(0, 10, "id,asc");

        // Verify
        assertNotNull(response);
        assertEquals(HttpStatus.OK, Objects.requireNonNull(response.getBody()).getStatus());
        assertEquals("Fetched all carts", response.getBody().getDescription());
        verify(cartService, times(1)).getAllCarts(pageable, mockUserPrincipal);
    }


    @Test
    @WithMockUser(username = "testUser", roles = {"ROLE_CUSTOMER"})
    void testFindCartByCustomerId() {
        // Mock service response
        ResponseDto responseDto = ResponseDto.builder()
                .status(HttpStatus.OK)
                .description("Cart found for customer.")
                .payload(null)
                .build();
        when(cartService.findCartPerCustomer("testUser")).thenReturn(ResponseEntity.ok(responseDto));

        // Call the method with the mock Authentication (from @WithMockUser)
        ResponseEntity<ResponseDto> response = cartController.findCartByCustomerId(SecurityContextHolder.getContext().getAuthentication());

        // Verify
        assertNotNull(response);
        assertEquals(HttpStatus.OK, Objects.requireNonNull(response.getBody()).getStatus());
        assertEquals("Cart found for customer", response.getBody().getDescription());
        verify(cartService, times(1)).findCartPerCustomer("testUser");
    }

    @Test
    void testRemoveItemFromCart() {
        // Mock User object
        User mockUser = new User();
        mockUser.setUserId(1L);
        mockUser.setFullName("Test User");
        mockUser.setUsername("testUser");
        mockUser.setEmail("testuser@example.com");
        mockUser.setContact("123456789");
        mockUser.setPassword("password");
        mockUser.setRoles(Set.of(new Role(1L, RoleName.ROLE_CUSTOMER)));

        // Create UserPrincipal using UserPrincipal.create()
        UserPrincipal mockUserPrincipal = UserPrincipal.create(mockUser);

        // Mock CurrentUserV2
        when(CurrentUserV2.getCurrentUser()).thenReturn(mockUserPrincipal);

        // Mock service response
        ResponseDto responseDto = ResponseDto.builder()
                .status(HttpStatus.OK)
                .description("Item removed from cart successfully")
                .payload(null)
                .build();
        when(cartService.removeItemFromCart(1L)).thenReturn(ResponseEntity.ok(responseDto));

        // Call the method
        ResponseEntity<ResponseDto> response = cartController.removeItemFromCart(1L, authentication);

        // Verify
        assertNotNull(response);
        assertEquals(HttpStatus.OK, Objects.requireNonNull(response.getBody()).getStatus());
        assertEquals("Item removed from cart successfully", response.getBody().getDescription());
        verify(cartService, times(1)).removeItemFromCart(1L);
    }

    @Test
    void testClearCart() {
        // Mock service response
        ResponseDto responseDto = ResponseDto.builder()
                .status(HttpStatus.OK)
                .description("Cart cleared successfully")
                .payload(null)
                .build();
        when(cartService.clearCart(1L)).thenReturn(ResponseEntity.ok(responseDto));

        // Call the method
        ResponseEntity<ResponseDto> response = cartController.clearCart(1L);

        // Verify
        assertNotNull(response);
        assertEquals(HttpStatus.OK, Objects.requireNonNull(response.getBody()).getStatus());
        assertEquals("Cart cleared successfully", response.getBody().getDescription());
        verify(cartService, times(1)).clearCart(1L);
    }
}
