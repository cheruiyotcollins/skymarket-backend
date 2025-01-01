package com.gigster.skymarket.controller;

import com.gigster.skymarket.dto.ResponseDto;
import com.gigster.skymarket.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value="/api/v1/products/categories")
public class CategoryController {
    @Autowired
    CategoryService categoryService;
    @PostMapping
    public ResponseEntity<ResponseDto> addCategory(@RequestParam(value = "categoryName", required = true) String categoryName) {
        // Call the service to add the category
       return categoryService.addCategory(categoryName);

    }
    @GetMapping
    public ResponseEntity<ResponseDto> getAllCategories(Pageable pageable) {
        return categoryService.getAllCategories(pageable);
    }
    @PutMapping("/{id}")
    public ResponseEntity<ResponseDto> updateCategory(
            @PathVariable("id") Long categoryId,
            @RequestParam(value = "categoryName", required = true) String newCategoryName) {
        return categoryService.updateCategory(categoryId, newCategoryName);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDto> deleteCategory(@PathVariable("id") Long categoryId) {
        return categoryService.deleteCategory(categoryId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDto> findCategoryById(@PathVariable("id") Long categoryId) {
        return categoryService.findById(categoryId);
    }
}
