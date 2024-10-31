package com.gigster.skymarket.service.impl;

import com.gigster.skymarket.dto.ResponseDto;
import com.gigster.skymarket.enums.CategoryName;
import com.gigster.skymarket.mapper.ResponseDtoMapper;
import com.gigster.skymarket.model.Category;
import com.gigster.skymarket.repository.CategoryRepository;
import com.gigster.skymarket.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    ResponseDtoMapper responseDtoSetter;
    @Override
    public ResponseEntity<ResponseDto> addCategory(String categoryName) {
        try {
            Category category = new Category();
            category.setCategoryName(CategoryName.fromString(categoryName));
            categoryRepository.save(category);
            return responseDtoSetter.responseDtoSetter(HttpStatus.CREATED, "Category added successfully");
        } catch (Exception e) {
            e.printStackTrace();

            return responseDtoSetter.responseDtoSetter(
                    HttpStatus.BAD_REQUEST,
                    "Failed to add category: " + e.getMessage()
            );
        }
    }

    @Override
    public ResponseEntity<ResponseDto> getAll() {
        return responseDtoSetter.responseDtoSetter(HttpStatus.FOUND, "List of all categories",categoryRepository.findAll());
    }

    @Override
    public ResponseEntity<ResponseDto> updateCategory(Long categoryId, String newCategoryName) {
        Optional<Category> categoryOpt = categoryRepository.findById(categoryId);
        if (categoryOpt.isPresent()) {
            Category category = categoryOpt.get();
            category.setCategoryName(CategoryName.fromString(newCategoryName));
            categoryRepository.save(category);
            return responseDtoSetter.responseDtoSetter(HttpStatus.OK, "Category updated successfully", category);
        } else {
            return responseDtoSetter.responseDtoSetter(HttpStatus.NOT_FOUND, "Category not found");
        }
    }

    @Override
    public ResponseEntity<ResponseDto> deleteCategory(Long categoryId) {
        if (categoryRepository.existsById(categoryId)) {
            categoryRepository.deleteById(categoryId);
            return responseDtoSetter.responseDtoSetter(HttpStatus.OK, "Category deleted successfully");
        } else {
            return responseDtoSetter.responseDtoSetter(HttpStatus.NOT_FOUND, "Category not found");
        }
    }

    @Override
    public ResponseEntity<ResponseDto> findById(Long categoryId) {
        Optional<Category> categoryOpt = categoryRepository.findById(categoryId);
        if (categoryOpt.isPresent()) {
            return responseDtoSetter.responseDtoSetter(HttpStatus.FOUND, "Category retrieved successfully", categoryOpt.get());
        } else {
            return responseDtoSetter.responseDtoSetter(HttpStatus.NOT_FOUND, "Category not found");
        }
    }

}
