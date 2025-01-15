package com.gigster.skymarket.controller;

import com.gigster.skymarket.dto.*;
import com.gigster.skymarket.model.MyUser;
import com.gigster.skymarket.model.Role;
import com.gigster.skymarket.service.MyUserService;
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
public class MyUserController {
    @Autowired
    MyUserService myUserService;

    @Operation(summary = "New MyUser Registration")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "MyUser Created Successfully",
                    content = {@Content(mediaType = "application/json",schema = @Schema(implementation = MyUser.class))}),
            @ApiResponse(responseCode = "401",description = "Unauthorized myUser",content = @Content),
            @ApiResponse(responseCode = "404",description = "MyUser not found",content = @Content),
            @ApiResponse(responseCode = "400",description = "Bad Request",content = @Content)})
    @PostMapping(value = "/signup")
    public ResponseEntity<ResponseDto> register(@RequestBody SignUpRequestDto signUpRequestDto){
        return myUserService.register(signUpRequestDto);
    }

    @Operation(summary = "MyUser sign in")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "MyUser Logged In Successfully",
                    content = {@Content(mediaType = "application/json",schema = @Schema(implementation = MyUser.class))}),
            @ApiResponse(responseCode = "401",description = "Unauthorized myUser",content = @Content),
            @ApiResponse(responseCode = "404",description = "MyUser not found",content = @Content),
            @ApiResponse(responseCode = "400",description = "Bad Request",content = @Content)})
    @PreAuthorize("permitAll()")
    @PostMapping(value = "/signin")
    public ResponseEntity<?> login(@RequestBody LoginDto loginDto) {
        LoginResponseDto loginResponseDto = myUserService.login(loginDto);

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

    @Operation(summary = "Update MyUser Info")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "MyUser Information Updated Successfully",
                    content = {@Content(mediaType = "application/json",schema = @Schema(implementation = MyUser.class))}),
            @ApiResponse(responseCode = "401",description = "Unauthorized myUser",content = @Content),
            @ApiResponse(responseCode = "404",description = "MyUser not found",content = @Content),
            @ApiResponse(responseCode = "400",description = "Bad Request",content = @Content)})

    @PutMapping
    public ResponseEntity<?> updateUser(@RequestBody SignUpRequestDto signUpRequestDto){
        return myUserService.register(signUpRequestDto);

    }

    @Operation(summary = "Retrieve MyUser by ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "302", description = "MyUser found",
                    content = {@Content(mediaType = "application/json",schema = @Schema(implementation = MyUser.class))}),
            @ApiResponse(responseCode = "401",description = "Unauthorized myUser",content = @Content),
            @ApiResponse(responseCode = "404",description = "MyUser not found",content = @Content),
            @ApiResponse(responseCode = "400",description = "Bad Request",content = @Content)})
    @GetMapping("/{id}")
    public ResponseEntity<?> findUserById(@PathVariable long id){
        return myUserService.findUserById(id);

    }

    @Operation(summary = "List All Users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "302", description = "returned list of users",
                    content = {@Content(mediaType = "application/json",schema = @Schema(implementation = MyUser.class))}),
            @ApiResponse(responseCode = "401",description = "Unauthorized myUser",content = @Content),
            @ApiResponse(responseCode = "404",description = "no myUser found",content = @Content),
            @ApiResponse(responseCode = "400",description = "Bad Request",content = @Content)})
    @GetMapping
    public ResponseEntity<ResponseDto> getAllUsers(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sort", defaultValue = "id,asc") String sort) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort.split(",")));
        return myUserService.getAllUsers(pageable);
    }

    @Operation(summary = "Delete MyUser By Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "MyUser Deleted Successfully",
                    content = {@Content(mediaType = "application/json",schema = @Schema(implementation = MyUser.class))}),
            @ApiResponse(responseCode = "401",description = "Unauthorized myUser",content = @Content),
            @ApiResponse(responseCode = "404",description = "MyUser not found",content = @Content),
            @ApiResponse(responseCode = "400",description = "Bad Request",content = @Content)})
    @DeleteMapping("/id/{id}")
    public ResponseEntity<?> deleteById(@PathVariable long id){
        return myUserService.deleteById(id);

    }

    @Operation(summary = "Create New Role")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Role Created Successfully",
                    content = {@Content(mediaType = "application/json",schema = @Schema(implementation = Role.class))}),
            @ApiResponse(responseCode = "401",description = "Unauthorized myUser",content = @Content),
            @ApiResponse(responseCode = "404",description = "Role not found",content = @Content),
            @ApiResponse(responseCode = "400",description = "Bad Request",content = @Content)})
    @PostMapping("/new/role")
    public ResponseEntity<?> addRole(@RequestBody AddRoleRequestDto addRoleRequestDto){
        return myUserService.addRole(addRoleRequestDto);
    }

    @Operation(summary = "Find Current MyUser")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Role Created Successfully",
                    content = {@Content(mediaType = "application/json",schema = @Schema(implementation = Role.class))}),
            @ApiResponse(responseCode = "401",description = "Unauthorized myUser",content = @Content),
            @ApiResponse(responseCode = "404",description = "Role not found",content = @Content),
            @ApiResponse(responseCode = "400",description = "Bad Request",content = @Content)})
    @GetMapping("/current")
    public ResponseEntity<?> getCurrentUser( Authentication authentication){

        return myUserService.getCurrentUser(authentication.getName());
    }

    @PutMapping("/update-password")
    public ResponseEntity<?> updatePassword(@RequestBody @Valid ChangePasswordDto changePasswordDto, Principal principal) {
        return myUserService.updatePassword(changePasswordDto.getNewPassword(),principal);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestParam String email) {
        return myUserService.forgotPassword(email);
    }

    @PutMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestParam String email,
                                           @RequestParam String resetCode,
                                           @RequestParam String newPassword) {
        return myUserService.resetPassword(email, resetCode, newPassword);
    }

}
