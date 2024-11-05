package com.gigster.skymarket.service;

import com.gigster.skymarket.dto.*;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface UserService {

    ResponseEntity<ResponseDto> register(SignUpRequest signUpRequest);

    String login(LoginDto loginDto);

    ResponseEntity<?> addRole(AddRoleRequest addRoleRequest);

    ResponseEntity<?> findUserById(long id);

    ResponseEntity<ResponseDto> getAllUsers(Pageable pageable);

    ResponseEntity<?> deleteById(long id);

    ResponseEntity<?> getCurrentUser(String email);
}
