package com.gigster.skymarket.service;

import com.gigster.skymarket.dto.NewProductDto;
import com.gigster.skymarket.dto.ResponseDto;
import org.springframework.http.ResponseEntity;

public interface ProductService {

    ResponseEntity<ResponseDto> createProduct(NewProductDto newProductDto);

    ResponseEntity<ResponseDto> getProductById(Long id);

    ResponseEntity<ResponseDto> getAllProducts(int page, int size, String sort);

    ResponseEntity<ResponseDto> updateProduct(Long id, NewProductDto newProductDto);

    ResponseEntity<ResponseDto> deleteProduct(Long id);

    ResponseEntity<ResponseDto> restockProduct(Long id, int stock);

    ResponseEntity<ResponseDto> likeProduct(Long productId, Long userId);

    ResponseEntity<ResponseDto> dislikeProduct(Long productId, Long userId);
}