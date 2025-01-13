package com.gigster.skymarket.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
@NoArgsConstructor
public class LoginResponseDto {
    private String accessToken;
    private boolean firstLogin;
    private int success;
    private String message;


    public LoginResponseDto(String token, boolean firstLogin) {
        this.firstLogin=firstLogin;
        this.accessToken=token;
    }
    public LoginResponseDto(String token, boolean firstLogin, int success, String message) {
        this.firstLogin=firstLogin;
        this.accessToken=token;
        this.message=message;
    }
}