package com.gigster.skymarket.mapper;

import com.gigster.skymarket.dto.ProductDto;
import com.gigster.skymarket.enums.CategoryName;
import com.gigster.skymarket.model.Product;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProductMapper {

    // Map Product entity to ProductDto
    public ProductDto toDto(Product product) {
        if (product == null) {
            return null;
        }

        return ProductDto.builder()
                .id(product.getId())
                .name(product.getProductName())
                .description(product.getDescription())
                .price(product.getPrice())
                .category(product.getCategory() != null ? product.getCategory().getCategoryName().name() : null)
                .imageUrl(product.getImageUrl())
                .build();
    }

    // Map ProductDto to Product entity
    public Product toEntity(ProductDto productDto) {
        if (productDto == null) {
            return null;
        }

        Product product = new Product();
        product.setId(productDto.getId());
        product.setProductName(productDto.getName());
        product.setDescription(productDto.getDescription());
        product.setPrice(productDto.getPrice());
        // Assuming categoryName comes as a string, use the fromString method to convert
        product.setCategory(CategoryName.fromString(productDto.getCategory()));
        product.setImageUrl(productDto.getImageUrl());
        return product;
    }

    // Map a list of Product entities to a list of ProductDto objects
    public List<ProductDto> toDtoList(List<Product> products) {
        return products.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    // Map a list of ProductDto objects to a list of Product entities
    public List<Product> toEntityList(List<ProductDto> productDtos) {
        return productDtos.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }
}
