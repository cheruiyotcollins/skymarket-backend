package com.gigster.skymarket.service;

import com.gigster.skymarket.dto.ResponseDto;
import com.gigster.skymarket.model.Catalogue;
import com.gigster.skymarket.model.Category;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CatalogueService {

    Catalogue createCatalogue(Catalogue catalogue);

    Catalogue getCatalogueById(Long id);

    ResponseEntity<ResponseDto> getAllCatalogues(int page, int size, String sort);

    Catalogue updateCatalogue(Long id, Catalogue updatedCatalogue);

    void deleteCatalogue(Long id);

    Catalogue assignCategoriesToCatalogue(Long catalogueId, List<Long> categoryIds);

    Catalogue addCategoryToCatalogue(Long catalogueId, Long categoryId);

    Catalogue removeCategoryFromCatalogue(Long catalogueId, Long categoryId);

    List<Category> getCategoriesInCatalogue(Long catalogueId);

    ResponseDto getProductsInCatalogue(Long catalogueId, Pageable pageable);

    Catalogue addProductsToCatalogue(Long catalogueId, List<Long> productIds);

    List<Catalogue> filterCataloguesByCategory(String categoryName);

    void validateAndDeleteCatalogue(Long id);

    void notifyOnCatalogueUpdate(Long id);
}
