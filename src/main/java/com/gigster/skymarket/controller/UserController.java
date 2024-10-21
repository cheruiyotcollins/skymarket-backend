package com.gigster.skymarket.controller;

import com.gigster.skymarket.dto.JWTAuthResponse;
import com.gigster.skymarket.dto.LoginDto;
import com.gigster.skymarket.dto.SignUpRequest;
import com.gigster.skymarket.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/user/")
@Slf4j
public class UserController {
    @Autowired
    UserService userService;

    @PostMapping("signup")
    public ResponseEntity<?> register(@RequestBody SignUpRequest signUpRequest){
        return userService.register(signUpRequest);
    }
    @PostMapping(value = {"/login", "/signin"})
    public ResponseEntity<JWTAuthResponse> login(@RequestBody LoginDto loginDto){
        String token = userService.login(loginDto);

        JWTAuthResponse jwtAuthResponse = new JWTAuthResponse();
        jwtAuthResponse.setAccessToken(token);

        return ResponseEntity.ok(jwtAuthResponse);
    }

}
