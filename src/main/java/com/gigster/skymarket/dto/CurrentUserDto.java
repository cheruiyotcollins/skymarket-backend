package com.gigster.skymarket.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CurrentUserDto {
    private String name;
    private String email;
    private String username;
    private long role;
}
