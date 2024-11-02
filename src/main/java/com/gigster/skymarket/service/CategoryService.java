package com.gigster.skymarket.service;

import com.gigster.skymarket.dto.ResponseDto;
import org.springframework.http.ResponseEntity;

public interface CategoryService{
    ResponseEntity<ResponseDto> addCategory(String categoryName);

    ResponseEntity<ResponseDto> getAll();
    ResponseEntity<ResponseDto> updateCategory(Long categoryId, String newCategoryName);

    ResponseEntity<ResponseDto> deleteCategory(Long categoryId);

    ResponseEntity<ResponseDto> findById(Long categoryId);
}
