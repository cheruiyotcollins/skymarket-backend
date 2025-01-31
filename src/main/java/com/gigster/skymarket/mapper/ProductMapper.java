package com.gigster.skymarket.mapper;

import com.gigster.skymarket.dto.ProductDto;
import com.gigster.skymarket.dto.RatingDto;
import com.gigster.skymarket.enums.CategoryName;
import com.gigster.skymarket.model.Category;
import com.gigster.skymarket.model.Product;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
//todo to use mapstruct
public class ProductMapper {

    // Map Product entity to ProductDto
    public ProductDto toDto(Product product) {
        if (product == null) {
            return null;
        }

        return ProductDto.builder()

                .id(product.getId())
                .title(product.getProductName())
                .description(product.getDescription())
                .price(product.getPrice())
                .category(product.getCategory() != null ? product.getCategory().getCategoryName().name() : null)
                .image(product.getImageUrl())
                .ratingDto(RatingDto.builder().rate(product.getRating()).count(product.getCount()).build())
                .build();
    }

    // Map ProductDto to Product entity
    public Product toEntity(ProductDto productDto) {
        if (productDto == null) {
            return null;
        }

        Product product = new Product();
        product.setProductName(productDto.getTitle());
        product.setDescription(productDto.getDescription());
        product.setPrice(productDto.getPrice());

        // Create a Category object using CategoryName from the DTO
        if (productDto.getCategory() != null) {
            Category category = new Category();
            category.setCategoryName(CategoryName.fromString(String.valueOf(productDto.getCategory())));
            product.setCategory(category);
        } else {
            product.setCategory(null);
        }

        product.setImageUrl(productDto.getImage());
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
