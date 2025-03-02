package com.gigster.skymarket.service.impl;

import com.gigster.skymarket.dto.CategoryDto;
import com.gigster.skymarket.dto.ResponseDto;
import com.gigster.skymarket.enums.CategoryName;
import com.gigster.skymarket.mapper.CategoryMapper;
import com.gigster.skymarket.mapper.ResponseDtoMapper;
import com.gigster.skymarket.model.Category;
import com.gigster.skymarket.repository.CategoryRepository;
import com.gigster.skymarket.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    ResponseDtoMapper responseDtoSetter;

    @Autowired
    CategoryMapper categoryMapper;

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
    public ResponseEntity<ResponseDto> findCategoryById(Long categoryId) {
        Optional<Category> categoryOpt = categoryRepository.findById(categoryId);
        if (categoryOpt.isPresent()) {
            return responseDtoSetter.responseDtoSetter(HttpStatus.FOUND, "Category retrieved successfully", categoryOpt.get());
        } else {
            return responseDtoSetter.responseDtoSetter(HttpStatus.NOT_FOUND, "Category not found");
        }
    }

    @Override
    public ResponseEntity<ResponseDto> getAllCategories(int page, int size, String sort) {
        String[] sortParams = sort.split(",");
        Sort.Direction direction = Sort.Direction.ASC;

        if (sortParams.length > 1 && sortParams[1].equalsIgnoreCase("desc")) {
            direction = Sort.Direction.DESC;
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortParams[0]));
        Page<Category> categoriesPage = categoryRepository.findAll(pageable);

        List<CategoryDto> categoryDtos = categoriesPage.getContent()
                .stream()
                .map(categoryMapper::toDto)
                .collect(Collectors.toList());

        ResponseDto responseDto = ResponseDto.builder()
                .status(HttpStatus.OK)
                .description("List of All Categories.")
                .payload(categoryDtos)
                .totalPages(categoriesPage.getTotalPages())
                .totalElements(categoriesPage.getTotalElements())
                .currentPage(categoriesPage.getNumber())
                .pageSize(categoriesPage.getSize())
                .build();

        return ResponseEntity.ok(responseDto);
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

}
