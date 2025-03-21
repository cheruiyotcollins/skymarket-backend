package com.gigster.skymarket.controller;

import com.gigster.skymarket.dto.ResponseDto;
import com.gigster.skymarket.model.Catalogue;
import com.gigster.skymarket.service.CatalogueService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/catalogues")
public class CatalogueController {

    private final CatalogueService catalogueService;

    public CatalogueController(CatalogueService catalogueService) {
        this.catalogueService = catalogueService;
    }

    @PostMapping
    public ResponseEntity<Catalogue> createCatalogue(@RequestBody Catalogue catalogue) {
        Catalogue createdCatalogue = catalogueService.createCatalogue(catalogue);
        return new ResponseEntity<>(createdCatalogue, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Catalogue> getCatalogueById(@PathVariable Long id) {
        Catalogue catalogue = catalogueService.getCatalogueById(id);
        return ResponseEntity.ok(catalogue);
    }

    @GetMapping
    public ResponseEntity<ResponseDto> getAllCatalogues(
            @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        return catalogueService.getAllCatalogues(pageable);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Catalogue> updateCatalogue(@PathVariable Long id, @RequestBody Catalogue catalogue) {
        Catalogue updatedCatalogue = catalogueService.updateCatalogue(id, catalogue);
        return ResponseEntity.ok(updatedCatalogue);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCatalogue(@PathVariable Long id) {
        catalogueService.deleteCatalogue(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/categories")
    public ResponseEntity<Catalogue> assignCategoriesToCatalogue(@PathVariable Long id, @RequestBody List<Long> categoryIds) {
        Catalogue updatedCatalogue = catalogueService.assignCategoriesToCatalogue(id, categoryIds);
        return ResponseEntity.ok(updatedCatalogue);
    }

    @PostMapping("/catalogues/{catalogueId}/categories/{categoryId}")
    public ResponseEntity<Catalogue> addCategoryToCatalogue(@PathVariable Long catalogueId, @PathVariable Long categoryId) {
        Catalogue updatedCatalogue = catalogueService.addCategoryToCatalogue(catalogueId, categoryId);
        return ResponseEntity.ok(updatedCatalogue);
    }

    @DeleteMapping("/catalogues/{catalogueId}/categories/{categoryId}")
    public ResponseEntity<Catalogue> removeCategoryFromCatalogue(@PathVariable Long catalogueId, @PathVariable Long categoryId) {
        Catalogue updatedCatalogue = catalogueService.removeCategoryFromCatalogue(catalogueId, categoryId);
        return ResponseEntity.ok(updatedCatalogue);
    }

    @GetMapping("/{id}/categories")
    public ResponseEntity<ResponseDto> getCategoriesInCatalogue(
            @PathVariable Long id,
            @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        return catalogueService.getCategoriesInCatalogue(id, pageable);
    }

    @GetMapping("/{id}/products")
    public ResponseEntity<ResponseDto> getProductsInCatalogue(@PathVariable Long id, Pageable pageable) {
        ResponseDto responseDto = catalogueService.getProductsInCatalogue(id, pageable);
        return ResponseEntity.ok(responseDto);
    }

    @PostMapping("/catalogues/{catalogueId}/products")
    public ResponseEntity<Catalogue> addProductsToCatalogue(@PathVariable Long catalogueId, @RequestBody List<Long> productIds) {
        Catalogue updatedCatalogue = catalogueService.addProductsToCatalogue(catalogueId, productIds);
        return ResponseEntity.ok(updatedCatalogue);
    }

    @GetMapping("/catalogues/category")
    public ResponseEntity<List<Catalogue>> filterCataloguesByCategory(@RequestParam String categoryName) {
        List<Catalogue> catalogues = catalogueService.filterCataloguesByCategory(categoryName);
        return ResponseEntity.ok(catalogues);
    }

    @DeleteMapping("/catalogues/{id}")
    public ResponseEntity<Void> validateAndDeleteCatalogue(@PathVariable Long id) {
        catalogueService.validateAndDeleteCatalogue(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/notify")
    public ResponseEntity<Void> notifyOnCatalogueUpdate(@PathVariable Long id) {
        catalogueService.notifyOnCatalogueUpdate(id);
        return ResponseEntity.ok().build();
    }
}