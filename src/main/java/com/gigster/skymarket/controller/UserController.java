package com.gigster.skymarket.controller;

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

    @PostMapping("new")
    public ResponseEntity<?> register(@RequestBody SignUpRequest signUpRequest){
        return userService.register(signUpRequest);
    }
}
