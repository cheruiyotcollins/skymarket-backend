package com.gigster.skymarket.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
@NoArgsConstructor
public class LoginResponse {
    private String token;
    private boolean firstLogin;
    private int success;
    private String message;


    public LoginResponse(String token, boolean firstLogin) {
        this.firstLogin=firstLogin;
        this.token=token;
    }
    public LoginResponse(String token, boolean firstLogin, int success,String message) {
        this.firstLogin=firstLogin;
        this.token=token;
        this.message=message;
    }
}