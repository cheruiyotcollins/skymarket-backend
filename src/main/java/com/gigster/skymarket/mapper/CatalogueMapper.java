package com.gigster.skymarket.mapper;

import com.gigster.skymarket.utils.DateUtils;
import com.gigster.skymarket.dto.CatalogueDto;
import com.gigster.skymarket.model.Catalogue;
import com.gigster.skymarket.model.Category;
import com.gigster.skymarket.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface CatalogueMapper {

    @Mapping(source = "categories", target = "categoryIds", qualifiedByName = "categoriesToIds")
    @Mapping(source = "products", target = "productIds", qualifiedByName = "productsToIds")
    @Mapping(source = "createdOn", target = "createdOn", qualifiedByName = "formatDate")
    @Mapping(source = "updatedAt", target = "updatedAt", qualifiedByName = "formatDate")
    CatalogueDto toDto(Catalogue catalogue);

    List<CatalogueDto> toDtoList(List<Catalogue> catalogues);

    @Named("categoriesToIds")
    default Set<Long> categoriesToIds(Set<Category> categories) {
        return categories.stream()
                .map(Category::getCategoryId)
                .collect(Collectors.toSet());
    }

    @Named("productsToIds")
    default Set<Long> productsToIds(Set<Product> products) {
        return products.stream()
                .map(Product::getId)
                .collect(Collectors.toSet());
    }

    @Named("formatDate")
    default String formatDate(String date) {
        return DateUtils.formatDateTime(date);
    }
}
