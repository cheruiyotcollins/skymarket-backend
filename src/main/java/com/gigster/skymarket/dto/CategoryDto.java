package com.gigster.skymarket.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDto {
    private Long id;
    private String categoryName;
    private String description;
    private String updatedDate;
//    private Long parentid;
}
