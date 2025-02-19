//package com.gigster.skymarket.unit.controller;
//
//import com.gigster.skymarket.controller.CartItemController;
//import com.gigster.skymarket.dto.CartItemRequestDto;
//import com.gigster.skymarket.dto.ResponseDto;
//import com.gigster.skymarket.model.Cart;
//import com.gigster.skymarket.model.Product;
//import com.gigster.skymarket.repository.CartRepository;
//import com.gigster.skymarket.repository.ProductRepository;
//import com.gigster.skymarket.service.CartItemService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.domain.Sort;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.Mockito.*;
//
//class CartItemControllerTest {
//
//    @Mock
//    private CartItemService cartItemService;
//
//    @Mock
//    private CartRepository cartRepository;
//
//    @Mock
//    private ProductRepository productRepository;
//
//    @InjectMocks
//    private CartItemController cartItemController;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    void addOrUpdateCartItem_ShouldReturnResponseEntity() {
//        // Arrange
//        CartItemRequestDto request = new CartItemRequestDto(1L, 1L, 2);
//        Cart cart = new Cart();
//        Product product = new Product();
//        ResponseDto responseDto = new ResponseDto();
//        ResponseEntity<ResponseDto> expectedResponse = new ResponseEntity<>(responseDto, HttpStatus.OK);
//
//        when(cartRepository.findById(1L)).thenReturn(Optional.of(cart));
//        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
//        when(cartItemService.addOrUpdateCartItem(Optional.of(cart), Optional.of(product), 2))
//                .thenReturn(expectedResponse);
//
//        // Act
//        ResponseEntity<ResponseDto> actualResponse = cartItemController.addOrUpdateCartItem(request);
//
//        // Assert
//        assertEquals(expectedResponse, actualResponse);
//        verify(cartRepository, times(1)).findById(1L);
//        verify(productRepository, times(1)).findById(1L);
//        verify(cartItemService, times(1)).addOrUpdateCartItem(Optional.of(cart), Optional.of(product), 2);
//    }
//
//    @Test
//    void getCartItem_ShouldReturnResponseEntity() {
//        // Arrange
//        Long cartId = 1L;
//        Long itemId = 1L;
//        ResponseDto responseDto = new ResponseDto();
//        ResponseEntity<ResponseDto> expectedResponse = new ResponseEntity<>(responseDto, HttpStatus.OK);
//
//        when(cartItemService.getCartItem(cartId, itemId)).thenReturn(expectedResponse);
//
//        // Act
//        ResponseEntity<ResponseDto> actualResponse = cartItemController.getCartItem(cartId, itemId);
//
//        // Assert
//        assertEquals(expectedResponse, actualResponse);
//        verify(cartItemService, times(1)).getCartItem(cartId, itemId);
//    }
//
//    @Test
//    void getAllCartItems_ShouldReturnResponseEntity() {
//        // Arrange
//        Long cartId = 1L;
//        int page = 0;
//        int size = 10;
//        String sort = "id,asc";
//        Pageable pageable = PageRequest.of(page, size, Sort.by(sort.split(",")));
//        ResponseDto responseDto = new ResponseDto();
//        ResponseEntity<ResponseDto> expectedResponse = new ResponseEntity<>(responseDto, HttpStatus.OK);
//
//        when(cartItemService.getAllCartItems(cartId, pageable)).thenReturn(expectedResponse);
//
//        // Act
//        ResponseEntity<ResponseDto> actualResponse =
//                cartItemController.getAllCartItems(cartId, page, size, sort);
//
//        // Assert
//        assertEquals(expectedResponse, actualResponse);
//        verify(cartItemService, times(1)).getAllCartItems(cartId, pageable);
//    }
//
//    @Test
//    void updateCartItem_ShouldReturnResponseEntity() {
//        // Arrange
//        Long cartId = 1L;
//        Long itemId = 1L;
//        int quantity = 3;
//        ResponseDto responseDto = new ResponseDto();
//        ResponseEntity<ResponseDto> expectedResponse = new ResponseEntity<>(responseDto, HttpStatus.OK);
//
//        when(cartItemService.updateCartItem(cartId, itemId, quantity)).thenReturn(expectedResponse);
//
//        // Act
//        ResponseEntity<ResponseDto> actualResponse =
//                cartItemController.updateCartItem(cartId, itemId, quantity);
//
//        // Assert
//        assertEquals(expectedResponse, actualResponse);
//        verify(cartItemService, times(1)).updateCartItem(cartId, itemId, quantity);
//    }
//}
