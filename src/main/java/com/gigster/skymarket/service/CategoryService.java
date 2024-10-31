package com.gigster.skymarket.service;

import com.gigster.skymarket.dto.CartDto;
import com.gigster.skymarket.dto.ResponseDto;
import com.gigster.skymarket.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;

public interface CategoryService{
    ResponseEntity<ResponseDto> addCategory(String categoryName);

    ResponseEntity<ResponseDto> getAll();
    ResponseEntity<ResponseDto> updateCategory(Long categoryId, String newCategoryName);

    ResponseEntity<ResponseDto> deleteCategory(Long categoryId);

    ResponseEntity<ResponseDto> findById(Long categoryId);
}
