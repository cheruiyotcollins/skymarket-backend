package com.gigster.skymarket.dto;

import lombok.Data;

@Data
public class LoginResponse {
    private String token;
    private boolean firstLogin;

    public LoginResponse(String token, boolean firstLogin) {
        this.token = token;
        this.firstLogin = firstLogin;
    }

}