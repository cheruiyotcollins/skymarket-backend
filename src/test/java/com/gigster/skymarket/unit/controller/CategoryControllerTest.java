package com.gigster.skymarket.unit.controller;

import com.gigster.skymarket.controller.CategoryController;
import com.gigster.skymarket.dto.ResponseDto;
import com.gigster.skymarket.service.CategoryService;
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

class CategoryControllerTest {

    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private CategoryController categoryController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addCategory_ShouldReturnResponseEntity() {
        // Arrange
        String categoryName = "Electronics";
        ResponseDto responseDto = new ResponseDto();
        ResponseEntity<ResponseDto> expectedResponse = new ResponseEntity<>(responseDto, HttpStatus.OK);

        when(categoryService.addCategory(categoryName)).thenReturn(expectedResponse);

        // Act
        ResponseEntity<ResponseDto> actualResponse = categoryController.addCategory(categoryName);

        // Assert
        assertEquals(expectedResponse, actualResponse);
        verify(categoryService, times(1)).addCategory(categoryName);
    }

    @Test
    void findCategoryById_ShouldReturnResponseEntity() {
        // Arrange
        Long categoryId = 1L;
        ResponseDto responseDto = new ResponseDto();
        ResponseEntity<ResponseDto> expectedResponse = new ResponseEntity<>(responseDto, HttpStatus.OK);

        when(categoryService.findCategoryById(categoryId)).thenReturn(expectedResponse);

        // Act
        ResponseEntity<ResponseDto> actualResponse = categoryController.findCategoryById(categoryId);

        // Assert
        assertEquals(expectedResponse, actualResponse);
        verify(categoryService, times(1)).findCategoryById(categoryId);
    }

    @Test
    void getAllCategories_ShouldReturnResponseEntity() {
        // Arrange
        int page = 0;
        int size = 10;
        String sort = "id,asc";
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort.split(",")));
        ResponseDto responseDto = new ResponseDto();
        ResponseEntity<ResponseDto> expectedResponse = new ResponseEntity<>(responseDto, HttpStatus.OK);

        when(categoryService.getAllCategories(pageable)).thenReturn(expectedResponse);

        // Act
        ResponseEntity<ResponseDto> actualResponse = categoryController.getAllCategories(page, size, sort);

        // Assert
        assertEquals(expectedResponse, actualResponse);
        verify(categoryService, times(1)).getAllCategories(pageable);
    }

    @Test
    void updateCategory_ShouldReturnResponseEntity() {
        // Arrange
        Long categoryId = 1L;
        String newCategoryName = "Updated Electronics";
        ResponseDto responseDto = new ResponseDto();
        ResponseEntity<ResponseDto> expectedResponse = new ResponseEntity<>(responseDto, HttpStatus.OK);

        when(categoryService.updateCategory(categoryId, newCategoryName)).thenReturn(expectedResponse);

        // Act
        ResponseEntity<ResponseDto> actualResponse =
                categoryController.updateCategory(categoryId, newCategoryName);

        // Assert
        assertEquals(expectedResponse, actualResponse);
        verify(categoryService, times(1)).updateCategory(categoryId, newCategoryName);
    }

    @Test
    void deleteCategory_ShouldReturnResponseEntity() {
        // Arrange
        Long categoryId = 1L;
        ResponseDto responseDto = new ResponseDto();
        ResponseEntity<ResponseDto> expectedResponse = new ResponseEntity<>(responseDto, HttpStatus.OK);

        when(categoryService.deleteCategory(categoryId)).thenReturn(expectedResponse);

        // Act
        ResponseEntity<ResponseDto> actualResponse = categoryController.deleteCategory(categoryId);

        // Assert
        assertEquals(expectedResponse, actualResponse);
        verify(categoryService, times(1)).deleteCategory(categoryId);
    }
}
