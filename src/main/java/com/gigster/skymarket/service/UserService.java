package com.gigster.skymarket.service;

import com.gigster.skymarket.dto.*;
import com.gigster.skymarket.model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.security.Principal;

public interface UserService {

    ResponseEntity<ResponseDto> register(SignUpRequestDto signUpRequestDto);

    LoginResponseDto login(LoginDto loginDto);

    ResponseEntity<ResponseDto> addRole(AddRoleRequestDto addRoleRequestDto);

    ResponseEntity<ResponseDto> findUserById(long id);

    ResponseEntity<ResponseDto> getAllUsers(Pageable pageable);

    ResponseEntity<ResponseDto> deleteById(long id);

    ResponseEntity<ResponseDto> getCurrentUser(String email);

    ResponseEntity<ResponseDto> updatePassword(String newPassword, Principal principal);

    ResponseEntity<ResponseDto> forgotPassword(String email);

    ResponseEntity<ResponseDto> resetPassword(String email, String resetCode, String newPassword);

    User getCurrentAuthenticatedUser();
}