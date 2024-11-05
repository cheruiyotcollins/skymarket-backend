package com.gigster.skymarket.dto;

import com.gigster.skymarket.enums.RoleName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponseDto {

    private String email;
    private String username;
    private Set<RoleName> roles;
}
