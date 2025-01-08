package com.gigster.skymarket.controller;

import com.gigster.skymarket.dto.NewProductDto;
import com.gigster.skymarket.dto.ResponseDto;
import com.gigster.skymarket.service.ProductService;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ProductControllerTest {

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createProduct_ShouldReturnCreatedProduct() {
        // Arrange
        NewProductDto newProductDto = new NewProductDto(); // Add fields as necessary
        ResponseEntity<?> expectedResponse = new ResponseEntity<>(HttpStatus.CREATED);

        when(productService.createProduct(newProductDto)).thenReturn((ResponseEntity<ResponseDto>) expectedResponse);

        // Act
        ResponseEntity<?> actualResponse = productController.createProduct(newProductDto);

        // Assert
        assertEquals(expectedResponse, actualResponse);
        verify(productService, times(1)).createProduct(newProductDto);
    }

    @Test
    void getProductById_ShouldReturnProduct() {
        // Arrange
        Long productId = 1L;
        ResponseDto responseDto = new ResponseDto(); // Add fields as necessary
        ResponseEntity<ResponseDto> expectedResponse = new ResponseEntity<>(responseDto, HttpStatus.OK);

        when(productService.getProductById(productId)).thenReturn(expectedResponse);

        // Act
        ResponseEntity<ResponseDto> actualResponse = productController.getProductById(productId);

        // Assert
        assertEquals(expectedResponse, actualResponse);
        verify(productService, times(1)).getProductById(productId);
    }

    @Test
    void getAllProducts_ShouldReturnProductList() {
        // Arrange
        int page = 0;
        int size = 10;
        String sort = "id,asc";
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort.split(",")));
        ResponseDto responseDto = new ResponseDto(); // Add fields as necessary
        ResponseEntity<ResponseDto> expectedResponse = new ResponseEntity<>(responseDto, HttpStatus.OK);

        when(productService.getAllProducts(pageable)).thenReturn(expectedResponse);

        // Act
        ResponseEntity<ResponseDto> actualResponse = productController.getAllProducts(page, size, sort);

        // Assert
        assertEquals(expectedResponse, actualResponse);
        verify(productService, times(1)).getAllProducts(pageable);
    }

    @Test
    void updateProduct_ShouldReturnUpdatedProduct() {
        // Arrange
        Long productId = 1L;
        NewProductDto newProductDto = new NewProductDto(); // Add fields as necessary
        ResponseDto responseDto = new ResponseDto(); // Add fields as necessary
        ResponseEntity<ResponseDto> expectedResponse = new ResponseEntity<>(responseDto, HttpStatus.OK);

        when(productService.updateProduct(productId, newProductDto)).thenReturn(expectedResponse);

        // Act
        ResponseEntity<ResponseDto> actualResponse = productController.updateProduct(productId, newProductDto);

        // Assert
        assertEquals(expectedResponse, actualResponse);
        verify(productService, times(1)).updateProduct(productId, newProductDto);
    }

    @Test
    void deleteProduct_ShouldReturnNoContent() {
        // Arrange
        Long productId = 1L;
        ResponseDto responseDto = new ResponseDto(); // Add fields as necessary
        ResponseEntity<ResponseDto> expectedResponse = new ResponseEntity<>(responseDto, HttpStatus.NO_CONTENT);

        when(productService.deleteProduct(productId)).thenReturn(expectedResponse);

        // Act
        ResponseEntity<ResponseDto> actualResponse = productController.deleteProduct(productId);

        // Assert
        assertEquals(expectedResponse, actualResponse);
        verify(productService, times(1)).deleteProduct(productId);
    }

    @Test
    void restockProduct_ShouldReturnUpdatedStock() {
        // Arrange
        Long productId = 1L;
        int stock = 50;
        ResponseDto responseDto = new ResponseDto(); // Add fields as necessary
        ResponseEntity<ResponseDto> expectedResponse = new ResponseEntity<>(responseDto, HttpStatus.OK);

        when(productService.restockProduct(productId, stock)).thenReturn(expectedResponse);

        // Act
        ResponseEntity<ResponseDto> actualResponse = productController.restockProduct(productId, stock);

        // Assert
        assertEquals(expectedResponse, actualResponse);
        verify(productService, times(1)).restockProduct(productId, stock);
    }
}
