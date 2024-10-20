package com.gigster.skymarket.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CurrentUserDto {
    private String name;
    private String email;
    private String regNo;
    private long role;
}
