package com.gigster.skymarket.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AttributeDto {
    private Long id;
    private String name;
    private String value;
}
