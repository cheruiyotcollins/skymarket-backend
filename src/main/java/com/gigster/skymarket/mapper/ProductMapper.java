package com.gigster.skymarket.mapper;

import com.gigster.skymarket.dto.ProductDto;
import com.gigster.skymarket.dto.RatingDto;
import com.gigster.skymarket.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", uses = CategoryMapper.class)
public interface ProductMapper {

    CategoryMapper CATEGORY_MAPPER = Mappers.getMapper(CategoryMapper.class);

    @Mapping(source = "product.productName", target = "title")
    @Mapping(source = "product.category", target = "category", qualifiedByName = "categoryToString")
    @Mapping(source = "product.rating", target = "ratingDto.rate")
    @Mapping(source = "product.count", target = "ratingDto.count")
    ProductDto toDto(Product product);

    @Mapping(source = "productDto.title", target = "productName")
    @Mapping(source = "productDto.category", target = "category", qualifiedByName = "stringToCategory")
    @Mapping(source = "productDto.ratingDto.rate", target = "rating")
    @Mapping(source = "productDto.ratingDto.count", target = "count")
    Product toEntity(ProductDto productDto);

    List<ProductDto> toDtoList(List<Product> products);

    List<Product> toEntityList(List<ProductDto> productDtos);
}
