package com.gigster.skymarket.unit.controller;

import com.gigster.skymarket.controller.CatalogueController;
import com.gigster.skymarket.dto.ResponseDto;
import com.gigster.skymarket.model.Catalogue;
import com.gigster.skymarket.model.Category;
import com.gigster.skymarket.service.CatalogueService;
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

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class CatalogueControllerTest {

    @Mock
    private CatalogueService catalogueService;

    @InjectMocks
    private CatalogueController catalogueController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createCatalogue_ShouldReturnResponseEntity() {
        // Arrange
        Catalogue catalogue = new Catalogue();
        Catalogue createdCatalogue = new Catalogue();
        ResponseEntity<Catalogue> expectedResponse = new ResponseEntity<>(createdCatalogue, HttpStatus.CREATED);

        when(catalogueService.createCatalogue(catalogue)).thenReturn(createdCatalogue);

        // Act
        ResponseEntity<Catalogue> actualResponse = catalogueController.createCatalogue(catalogue);

        // Assert
        assertEquals(expectedResponse, actualResponse);
        verify(catalogueService, times(1)).createCatalogue(catalogue);
    }

    @Test
    void getCatalogueById_ShouldReturnResponseEntity() {
        // Arrange
        Long catalogueId = 1L;
        Catalogue catalogue = new Catalogue();
        ResponseEntity<Catalogue> expectedResponse = new ResponseEntity<>(catalogue, HttpStatus.OK);

        when(catalogueService.getCatalogueById(catalogueId)).thenReturn(catalogue);

        // Act
        ResponseEntity<Catalogue> actualResponse = catalogueController.getCatalogueById(catalogueId);

        // Assert
        assertEquals(expectedResponse, actualResponse);
        verify(catalogueService, times(1)).getCatalogueById(catalogueId);
    }

    @Test
    void getAllCatalogues_ShouldReturnResponseEntity() {
        // Arrange
        int page = 0;
        int size = 10;
        String sort = "id,asc";
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort.split(",")));
        ResponseDto responseDto = new ResponseDto();
        ResponseEntity<ResponseDto> expectedResponse = new ResponseEntity<>(responseDto, HttpStatus.OK);

        when(catalogueService.getAllCatalogues(pageable)).thenReturn(expectedResponse);

        // Act
        ResponseEntity<ResponseDto> actualResponse = catalogueController.getAllCatalogues(page, size, sort);

        // Assert
        assertEquals(expectedResponse, actualResponse);
        verify(catalogueService, times(1)).getAllCatalogues(pageable);
    }

    @Test
    void updateCatalogue_ShouldReturnResponseEntity() {
        // Arrange
        Long catalogueId = 1L;
        Catalogue catalogue = new Catalogue();
        Catalogue updatedCatalogue = new Catalogue();
        ResponseEntity<Catalogue> expectedResponse = new ResponseEntity<>(updatedCatalogue, HttpStatus.OK);

        when(catalogueService.updateCatalogue(catalogueId, catalogue)).thenReturn(updatedCatalogue);

        // Act
        ResponseEntity<Catalogue> actualResponse = catalogueController.updateCatalogue(catalogueId, catalogue);

        // Assert
        assertEquals(expectedResponse, actualResponse);
        verify(catalogueService, times(1)).updateCatalogue(catalogueId, catalogue);
    }

    @Test
    void deleteCatalogue_ShouldReturnResponseEntity() {
        // Arrange
        Long catalogueId = 1L;
        ResponseEntity<Void> expectedResponse = ResponseEntity.noContent().build();

        doNothing().when(catalogueService).deleteCatalogue(catalogueId);

        // Act
        ResponseEntity<Void> actualResponse = catalogueController.deleteCatalogue(catalogueId);

        // Assert
        assertEquals(expectedResponse, actualResponse);
        verify(catalogueService, times(1)).deleteCatalogue(catalogueId);
    }

    @Test
    void assignCategoriesToCatalogue_ShouldReturnResponseEntity() {
        // Arrange
        Long catalogueId = 1L;
        List<Long> categoryIds = List.of(1L, 2L);
        Catalogue updatedCatalogue = new Catalogue();
        ResponseEntity<Catalogue> expectedResponse = new ResponseEntity<>(updatedCatalogue, HttpStatus.OK);

        when(catalogueService.assignCategoriesToCatalogue(catalogueId, categoryIds)).thenReturn(updatedCatalogue);

        // Act
        ResponseEntity<Catalogue> actualResponse = catalogueController.assignCategoriesToCatalogue(catalogueId, categoryIds);

        // Assert
        assertEquals(expectedResponse, actualResponse);
        verify(catalogueService, times(1)).assignCategoriesToCatalogue(catalogueId, categoryIds);
    }

    @Test
    void getCategoriesInCatalogue_ShouldReturnResponseEntity() {
        // Arrange
        Long catalogueId = 1L;
        List<Category> categoryList = List.of(new Category(), new Category());
        Set<Category> categories = new HashSet<>(categoryList);
        ResponseEntity<Set<Category>> expectedResponse = new ResponseEntity<>(categories, HttpStatus.OK);

        when(catalogueService.getCategoriesInCatalogue(catalogueId)).thenReturn(categoryList);

        // Act
        ResponseEntity<Set<Category>> actualResponse = catalogueController.getCategoriesInCatalogue(catalogueId);

        // Assert
        assertEquals(expectedResponse, actualResponse);
        verify(catalogueService, times(1)).getCategoriesInCatalogue(catalogueId);
    }

    @Test
    void addCategoryToCatalogue_ShouldReturnResponseEntity() {
        // Arrange
        Long catalogueId = 1L;
        Long categoryId = 2L;
        Catalogue updatedCatalogue = new Catalogue();
        ResponseEntity<Catalogue> expectedResponse = new ResponseEntity<>(updatedCatalogue, HttpStatus.OK);

        when(catalogueService.addCategoryToCatalogue(catalogueId, categoryId)).thenReturn(updatedCatalogue);

        // Act
        ResponseEntity<Catalogue> actualResponse = catalogueController.addCategoryToCatalogue(catalogueId, categoryId);

        // Assert
        assertEquals(expectedResponse, actualResponse);
        verify(catalogueService, times(1)).addCategoryToCatalogue(catalogueId, categoryId);
    }

    @Test
    void removeCategoryFromCatalogue_ShouldReturnResponseEntity() {
        // Arrange
        Long catalogueId = 1L;
        Long categoryId = 2L;
        Catalogue updatedCatalogue = new Catalogue();
        ResponseEntity<Catalogue> expectedResponse = new ResponseEntity<>(updatedCatalogue, HttpStatus.OK);

        when(catalogueService.removeCategoryFromCatalogue(catalogueId, categoryId)).thenReturn(updatedCatalogue);

        // Act
        ResponseEntity<Catalogue> actualResponse = catalogueController.removeCategoryFromCatalogue(catalogueId, categoryId);

        // Assert
        assertEquals(expectedResponse, actualResponse);
        verify(catalogueService, times(1)).removeCategoryFromCatalogue(catalogueId, categoryId);
    }

    @Test
    void getProductsInCatalogue_ShouldReturnResponseEntity() {
        // Arrange
        Long catalogueId = 1L;
        int page = 0;
        int size = 10;
        String sort = "id,asc";
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort.split(",")));
        ResponseDto responseDto = new ResponseDto();
        ResponseEntity<ResponseDto> expectedResponse = new ResponseEntity<>(responseDto, HttpStatus.OK);

        when(catalogueService.getProductsInCatalogue(catalogueId, pageable)).thenReturn(responseDto);

        // Act
        ResponseEntity<ResponseDto> actualResponse = catalogueController.getProductsInCatalogue(catalogueId, pageable);

        // Assert
        assertEquals(expectedResponse, actualResponse);
        verify(catalogueService, times(1)).getProductsInCatalogue(catalogueId, pageable);
    }

    @Test
    void addProductsToCatalogue_ShouldReturnResponseEntity() {
        // Arrange
        Long catalogueId = 1L;
        List<Long> productIds = List.of(1L, 2L, 3L);
        Catalogue updatedCatalogue = new Catalogue();
        ResponseEntity<Catalogue> expectedResponse = new ResponseEntity<>(updatedCatalogue, HttpStatus.OK);

        when(catalogueService.addProductsToCatalogue(catalogueId, productIds)).thenReturn(updatedCatalogue);

        // Act
        ResponseEntity<Catalogue> actualResponse = catalogueController.addProductsToCatalogue(catalogueId, productIds);

        // Assert
        assertEquals(expectedResponse, actualResponse);
        verify(catalogueService, times(1)).addProductsToCatalogue(catalogueId, productIds);
    }


    @Test
    void filterCataloguesByCategory_ShouldReturnResponseEntity() {
        // Arrange
        String categoryName = "Electronics";
        List<Catalogue> catalogues = List.of(new Catalogue(), new Catalogue());
        ResponseEntity<List<Catalogue>> expectedResponse = new ResponseEntity<>(catalogues, HttpStatus.OK);

        when(catalogueService.filterCataloguesByCategory(categoryName)).thenReturn(catalogues);

        // Act
        ResponseEntity<List<Catalogue>> actualResponse = catalogueController.filterCataloguesByCategory(categoryName);

        // Assert
        assertEquals(expectedResponse, actualResponse);
        verify(catalogueService, times(1)).filterCataloguesByCategory(categoryName);
    }

    @Test
    void validateAndDeleteCatalogue_ShouldReturnResponseEntity() {
        // Arrange
        Long catalogueId = 1L;
        ResponseEntity<Void> expectedResponse = ResponseEntity.noContent().build();

        doNothing().when(catalogueService).validateAndDeleteCatalogue(catalogueId);

        // Act
        ResponseEntity<Void> actualResponse = catalogueController.validateAndDeleteCatalogue(catalogueId);

        // Assert
        assertEquals(expectedResponse, actualResponse);
        verify(catalogueService, times(1)).validateAndDeleteCatalogue(catalogueId);
    }

    @Test
    void notifyOnCatalogueUpdate_ShouldReturnResponseEntity() {
        // Arrange
        Long catalogueId = 1L;
        ResponseEntity<Void> expectedResponse = ResponseEntity.ok().build();

        doNothing().when(catalogueService).notifyOnCatalogueUpdate(catalogueId);

        // Act
        ResponseEntity<Void> actualResponse = catalogueController.notifyOnCatalogueUpdate(catalogueId);

        // Assert
        assertEquals(expectedResponse, actualResponse);
        verify(catalogueService, times(1)).notifyOnCatalogueUpdate(catalogueId);
    }
}
