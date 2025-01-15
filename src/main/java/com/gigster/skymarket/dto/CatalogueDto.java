package com.gigster.skymarket.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CatalogueDto {

    private Long id;
    private String name;
    private String description;
    private Set<Long> categoryIds;
    private Set<Long> productIds;
    private String createdOn;
    private String updatedAt;
}
