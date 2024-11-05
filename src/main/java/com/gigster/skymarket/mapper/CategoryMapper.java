package com.gigster.skymarket.mapper;

import com.gigster.skymarket.dto.CategoryDto;
import com.gigster.skymarket.model.Category;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    CategoryDto toDto(Category category);

    Category toEntity(CategoryDto categoryDto);

    List<CategoryDto> toDtoList(List<Category> categories);

    List<Category> toEntityList(List<CategoryDto> categoryDtos);
}
