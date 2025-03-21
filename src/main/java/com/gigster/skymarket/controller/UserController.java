package com.gigster.skymarket.controller;

import com.gigster.skymarket.dto.*;
import com.gigster.skymarket.model.User;
import com.gigster.skymarket.model.Role;
import com.gigster.skymarket.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
@RestController
@RequestMapping(value = "/api/v1/users/auth")
@Slf4j
public class UserController {
    @Autowired
    UserService userService;

    @Operation(summary = "New User Registration")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User Created Successfully",
                    content = {@Content(mediaType = "application/json",schema = @Schema(implementation = User.class))}),
            @ApiResponse(responseCode = "401",description = "Unauthorized user",content = @Content),
            @ApiResponse(responseCode = "404",description = "User not found",content = @Content),
            @ApiResponse(responseCode = "400",description = "Bad Request",content = @Content)})
    @PostMapping(value = "/signup")
    public ResponseEntity<ResponseDto> register(@RequestBody SignUpRequestDto signUpRequestDto){
        return userService.register(signUpRequestDto);
    }

    @Operation(summary = "User sign in")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User Logged In Successfully",
                    content = {@Content(mediaType = "application/json",schema = @Schema(implementation = User.class))}),
            @ApiResponse(responseCode = "401",description = "Unauthorized user",content = @Content),
            @ApiResponse(responseCode = "404",description = "User not found",content = @Content),
            @ApiResponse(responseCode = "400",description = "Bad Request",content = @Content)})
    @PreAuthorize("permitAll()")
    @PostMapping(value = "/signin")
    public ResponseEntity<?> login(@RequestBody LoginDto loginDto) {
        LoginResponseDto loginResponseDto = userService.login(loginDto);

        // If not the first login, return the JWT token in the response
        JWTAuthResponseDto jwtAuthResponseDto = new JWTAuthResponseDto();
        String token = loginResponseDto.getAccessToken();
        jwtAuthResponseDto.setAccessToken(token);
        if (loginResponseDto.isFirstLogin()) {
            loginResponseDto.setSuccess(0);
            loginResponseDto.setMessage("First login detected. Please change your password.");
            loginResponseDto.setAccessToken(token);
            return new ResponseEntity<>(loginResponseDto, HttpStatus.OK);
        }
        return ResponseEntity.ok(jwtAuthResponseDto);
    }

    @Operation(summary = "Update User Info")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User Information Updated Successfully",
                    content = {@Content(mediaType = "application/json",schema = @Schema(implementation = User.class))}),
            @ApiResponse(responseCode = "401",description = "Unauthorized user",content = @Content),
            @ApiResponse(responseCode = "404",description = "User not found",content = @Content),
            @ApiResponse(responseCode = "400",description = "Bad Request",content = @Content)})

    @PutMapping
    public ResponseEntity<?> updateUser(@RequestBody SignUpRequestDto signUpRequestDto){
        return userService.register(signUpRequestDto);

    }

    @Operation(summary = "Retrieve User by ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "302", description = "User found",
                    content = {@Content(mediaType = "application/json",schema = @Schema(implementation = User.class))}),
            @ApiResponse(responseCode = "401",description = "Unauthorized user",content = @Content),
            @ApiResponse(responseCode = "404",description = "User not found",content = @Content),
            @ApiResponse(responseCode = "400",description = "Bad Request",content = @Content)})
    @GetMapping("/{id}")
    public ResponseEntity<?> findUserById(@PathVariable long id){
        return userService.findUserById(id);

    }

    @Operation(summary = "List All Users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "302", description = "returned list of users",
                    content = {@Content(mediaType = "application/json",schema = @Schema(implementation = User.class))}),
            @ApiResponse(responseCode = "401",description = "Unauthorized user",content = @Content),
            @ApiResponse(responseCode = "404",description = "no user found",content = @Content),
            @ApiResponse(responseCode = "400",description = "Bad Request",content = @Content)})
    @GetMapping
    public ResponseEntity<ResponseDto> getAllUsers(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sort", defaultValue = "id,asc") String sort) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort.split(",")));
        return userService.getAllUsers(pageable);
    }

    @Operation(summary = "Delete User By Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User Deleted Successfully",
                    content = {@Content(mediaType = "application/json",schema = @Schema(implementation = User.class))}),
            @ApiResponse(responseCode = "401",description = "Unauthorized user",content = @Content),
            @ApiResponse(responseCode = "404",description = "User not found",content = @Content),
            @ApiResponse(responseCode = "400",description = "Bad Request",content = @Content)})
    @DeleteMapping("/id/{id}")
    public ResponseEntity<?> deleteById(@PathVariable long id){
        return userService.deleteById(id);

    }

    @Operation(summary = "Create New Role")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Role Created Successfully",
                    content = {@Content(mediaType = "application/json",schema = @Schema(implementation = Role.class))}),
            @ApiResponse(responseCode = "401",description = "Unauthorized user",content = @Content),
            @ApiResponse(responseCode = "404",description = "Role not found",content = @Content),
            @ApiResponse(responseCode = "400",description = "Bad Request",content = @Content)})
    @PostMapping("/new/role")
    public ResponseEntity<?> addRole(@RequestBody AddRoleRequestDto addRoleRequestDto){
        return userService.addRole(addRoleRequestDto);
    }

    @Operation(summary = "Find Current User")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Role Created Successfully",
                    content = {@Content(mediaType = "application/json",schema = @Schema(implementation = Role.class))}),
            @ApiResponse(responseCode = "401",description = "Unauthorized user",content = @Content),
            @ApiResponse(responseCode = "404",description = "Role not found",content = @Content),
            @ApiResponse(responseCode = "400",description = "Bad Request",content = @Content)})
    @GetMapping("/current")
    public ResponseEntity<?> getCurrentUser( Authentication authentication){

        return userService.getCurrentUser(authentication.getName());
    }

    @PutMapping("/update-password")
    public ResponseEntity<?> updatePassword(@RequestBody @Valid ChangePasswordDto changePasswordDto, Principal principal) {
        return userService.updatePassword(changePasswordDto.getNewPassword(),principal);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestParam String email) {
        return userService.forgotPassword(email);
    }

    @PutMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestParam String email,
                                           @RequestParam String resetCode,
                                           @RequestParam String newPassword) {
        return userService.resetPassword(email, resetCode, newPassword);
    }

}
