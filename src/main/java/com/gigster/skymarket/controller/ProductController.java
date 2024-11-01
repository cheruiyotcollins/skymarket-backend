package com.gigster.skymarket.controller;

import com.gigster.skymarket.dto.NewProductDto;
import com.gigster.skymarket.dto.ResponseDto;
import com.gigster.skymarket.model.Product;
import com.gigster.skymarket.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
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

    @GetMapping
    public ResponseEntity<?> getAllProducts() {
        return productService.getAllProducts();
    }

        @GetMapping("/{id}")
    public ResponseEntity<ResponseDto> getProductById(@PathVariable Long id) {
        return productService.getProductById(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseDto> updateProduct(@PathVariable Long id, @RequestBody NewProductDto newProductDto) {
        return productService.updateProduct(id, newProductDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDto> deleteProduct(@PathVariable Long id) {
        return productService.deleteProduct(id);
    }
}
