package com.gigster.skymarket.service;

import com.gigster.skymarket.dto.NewProductDto;
import com.gigster.skymarket.model.Product;
import com.gigster.skymarket.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product createProduct(NewProductDto newProductDto) {
        Product product = new Product();
        product.setProductName(newProductDto.getProductName());
        product.setCategory(newProductDto.getCategory());
        product.setManufacturer(newProductDto.getManufacturer());
        product.setPrice(newProductDto.getPrice());
        product.setStock(newProductDto.getStock());

        return productRepository.save(product);
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    public Product updateProduct(Long id, NewProductDto newProductDto) {
        Optional<Product> optionalProduct = productRepository.findById(id);

        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();
            product.setProductName(newProductDto.getProductName());
            product.setCategory(newProductDto.getCategory());
            product.setManufacturer(newProductDto.getManufacturer());
            product.setPrice(newProductDto.getPrice());
            product.setStock(newProductDto.getStock());
            return productRepository.save(product);
        } else {
            throw new RuntimeException("Product not found with id " + id);
        }
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
}
