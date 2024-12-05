package com.gigster.skymarket.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.GetMapping;

@Getter
@Setter
public class LoginResponse {
    private String token;
    private boolean firstLogin;

    public LoginResponse(String token, boolean firstLogin) {
        this.token = token;
        this.firstLogin = firstLogin;
    }

}