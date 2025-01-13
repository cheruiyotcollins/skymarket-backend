package com.gigster.skymarket.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CurrentUserDto {
    private String name;
    private String email;
    private String username;
    private String role;
}
