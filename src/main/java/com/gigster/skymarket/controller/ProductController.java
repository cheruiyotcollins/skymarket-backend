package com.gigster.skymarket.controller;

import com.gigster.skymarket.dto.NewProductDto;
import com.gigster.skymarket.dto.ResponseDto;
import com.gigster.skymarket.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<?> createProduct(@RequestBody NewProductDto newProductDto) {
        return productService.createProduct(newProductDto);
    }


    @GetMapping("/{id}")
    public ResponseEntity<ResponseDto> getProductById(@PathVariable Long id) {
        //todo the first index of a product from frontend is starting from 0 we temporarily add 1 to id as
        //we find another better way to handle this
        return productService.getProductById(id+1);
    }

    @GetMapping
    public ResponseEntity<ResponseDto> getAllProducts(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            //todo confirm if removing asc affects the response
            @RequestParam(value = "sort", defaultValue = "id") String sort) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort.split(",")));
        return productService.getAllProducts(pageable);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseDto> updateProduct(@PathVariable Long id, @RequestBody NewProductDto newProductDto) {
        return productService.updateProduct(id, newProductDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDto> deleteProduct(@PathVariable Long id) {
        return productService.deleteProduct(id);
    }

    @PatchMapping("/{id}/restock")
    public ResponseEntity<ResponseDto> restockProduct(@PathVariable Long id, @RequestParam int stock) {
        return productService.restockProduct(id, stock);
    }
}
