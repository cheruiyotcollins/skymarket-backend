package com.gigster.skymarket.service;

import com.gigster.skymarket.dto.ResponseDto;
import org.springframework.http.ResponseEntity;

public interface CategoryService {
    ResponseEntity<ResponseDto> addCategory(String categoryName);

    ResponseEntity<ResponseDto> findCategoryById(Long categoryId);

    ResponseEntity<ResponseDto> getAllCategories(int page, int size, String sort);

    ResponseEntity<ResponseDto> updateCategory(Long categoryId, String newCategoryName);

    ResponseEntity<ResponseDto> deleteCategory(Long categoryId);

}
