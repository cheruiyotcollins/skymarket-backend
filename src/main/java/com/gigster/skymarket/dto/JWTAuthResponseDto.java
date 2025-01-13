package com.gigster.skymarket.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JWTAuthResponseDto {
    private String accessToken;
    private String tokenType = "Bearer";
}