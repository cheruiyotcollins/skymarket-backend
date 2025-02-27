package com.gigster.skymarket.mapper;

import com.gigster.skymarket.dto.CategoryDto;
import com.gigster.skymarket.enums.CategoryName;
import com.gigster.skymarket.model.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    CategoryDto toDto(Category category);

    Category toEntity(CategoryDto categoryDto);

    List<CategoryDto> toDtoList(List<Category> categories);

    List<Category> toEntityList(List<CategoryDto> categoryDtos);

    @Named("categoryToString")
    static String categoryToString(Category category) {
        return category != null ? category.getCategoryName().name() : null;
    }

    @Named("stringToCategory")
    static Category stringToCategory(String categoryName) {
        if (categoryName == null) {
            return null;
        }
        Category category = new Category();
        category.setCategoryName(CategoryName.fromString(categoryName)); // Assuming fromString exists in CategoryName
        return category;
    }
}
