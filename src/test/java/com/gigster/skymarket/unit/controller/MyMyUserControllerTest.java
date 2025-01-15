package com.gigster.skymarket.unit.controller;

import com.gigster.skymarket.controller.MyUserController;
import com.gigster.skymarket.dto.*;
import com.gigster.skymarket.service.MyUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.security.Principal;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class MyMyUserControllerTest {

    @Mock
    private MyUserService myUserService;

    @InjectMocks
    private MyUserController myUserController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void register_ShouldReturnResponseEntity() {
        // Arrange
        SignUpRequestDto signUpRequestDto = new SignUpRequestDto();
        ResponseDto responseDto = new ResponseDto();
        ResponseEntity<ResponseDto> expectedResponse = new ResponseEntity<>(responseDto, HttpStatus.CREATED);

        when(myUserService.register(signUpRequestDto)).thenReturn(expectedResponse);

        // Act
        ResponseEntity<ResponseDto> actualResponse = myUserController.register(signUpRequestDto);

        // Assert
        assertEquals(expectedResponse, actualResponse);
        verify(myUserService, times(1)).register(signUpRequestDto);
    }

    @Test
    void login_ShouldReturnResponseEntity() {
        // Arrange
        LoginDto loginDto = new LoginDto();
        LoginResponseDto loginResponseDto = new LoginResponseDto();
        loginResponseDto.setAccessToken("token");
        JWTAuthResponseDto jwtAuthResponseDto = new JWTAuthResponseDto();
        jwtAuthResponseDto.setAccessToken("token");

        when(myUserService.login(loginDto)).thenReturn(loginResponseDto);

        // Act
        ResponseEntity<?> actualResponse = myUserController.login(loginDto);

        // Assert - Compare the access token values instead of whole ResponseEntity.
        assertEquals(jwtAuthResponseDto.getAccessToken(), ((JWTAuthResponseDto) Objects.requireNonNull(actualResponse.getBody())).getAccessToken());
        verify(myUserService, times(1)).login(loginDto);
    }

    @Test
    void updateUser_ShouldReturnResponseEntity() {
        // Arrange
        SignUpRequestDto signUpRequestDto = new SignUpRequestDto();
        ResponseDto responseDto = new ResponseDto();
        ResponseEntity<ResponseDto> expectedResponse = new ResponseEntity<>(responseDto, HttpStatus.CREATED);

        when(myUserService.register(signUpRequestDto)).thenReturn(expectedResponse);

        // Act
        ResponseEntity<ResponseDto> actualResponse = (ResponseEntity<ResponseDto>) myUserController.updateUser(signUpRequestDto);

        // Assert
        assertEquals(expectedResponse, actualResponse);
        verify(myUserService, times(1)).register(signUpRequestDto);
    }

    @Test
    void findUserById_ShouldReturnResponseEntity() {
        // Arrange
        long userId = 1L;
        ResponseDto responseDto = new ResponseDto();
        ResponseEntity<ResponseDto> expectedResponse = new ResponseEntity<>(responseDto, HttpStatus.FOUND);

        when(myUserService.findUserById(userId)).thenReturn(expectedResponse);

        // Act
        ResponseEntity<ResponseDto> actualResponse = (ResponseEntity<ResponseDto>) myUserController.findUserById(userId);

        // Assert
        assertEquals(expectedResponse, actualResponse);
        verify(myUserService, times(1)).findUserById(userId);
    }

    @Test
    void getAllUsers_ShouldReturnResponseEntity() {
        // Arrange
        int page = 0;
        int size = 10;
        String sort = "id,asc";
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort.split(",")));
        ResponseDto responseDto = new ResponseDto();
        ResponseEntity<ResponseDto> expectedResponse = new ResponseEntity<>(responseDto, HttpStatus.FOUND);

        when(myUserService.getAllUsers(pageable)).thenReturn(expectedResponse);

        // Act
        ResponseEntity<ResponseDto> actualResponse = myUserController.getAllUsers(page, size, sort);

        // Assert
        assertEquals(expectedResponse, actualResponse);
        verify(myUserService, times(1)).getAllUsers(pageable);
    }

    @Test
    void deleteById_ShouldReturnResponseEntity() {
        // Arrange
        long userId = 1L;
        ResponseDto responseDto = new ResponseDto();
        ResponseEntity<ResponseDto> expectedResponse = new ResponseEntity<>(responseDto, HttpStatus.OK);

        when(myUserService.deleteById(userId)).thenReturn(expectedResponse);

        // Act
        ResponseEntity<ResponseDto> actualResponse = (ResponseEntity<ResponseDto>) myUserController.deleteById(userId);

        // Assert
        assertEquals(expectedResponse, actualResponse);
        verify(myUserService, times(1)).deleteById(userId);
    }

    @Test
    void addRole_ShouldReturnResponseEntity() {
        // Arrange
        AddRoleRequestDto addRoleRequestDto = new AddRoleRequestDto();
        ResponseDto responseDto = new ResponseDto();
        ResponseEntity<ResponseDto> expectedResponse = new ResponseEntity<>(responseDto, HttpStatus.CREATED);

        when(myUserService.addRole(addRoleRequestDto)).thenReturn(expectedResponse);

        // Act
        ResponseEntity<ResponseDto> actualResponse = (ResponseEntity<ResponseDto>) myUserController.addRole(addRoleRequestDto);

        // Assert
        assertEquals(expectedResponse, actualResponse);
        verify(myUserService, times(1)).addRole(addRoleRequestDto);
    }

    @Test
    void getCurrentUser_ShouldReturnResponseEntity() {
        // Arrange
        Authentication authentication = mock(Authentication.class);
        String username = "testUser";
        when(authentication.getName()).thenReturn(username);
        ResponseDto responseDto = new ResponseDto();
        ResponseEntity<ResponseDto> expectedResponse = new ResponseEntity<>(responseDto, HttpStatus.CREATED);

        when(myUserService.getCurrentUser(username)).thenReturn(expectedResponse);

        // Act
        ResponseEntity<ResponseDto> actualResponse = (ResponseEntity<ResponseDto>) myUserController.getCurrentUser(authentication);

        // Assert
        assertEquals(expectedResponse, actualResponse);
        verify(myUserService, times(1)).getCurrentUser(username);
    }

    @Test
    void updatePassword_ShouldReturnResponseEntity() {
        // Arrange
        ChangePasswordDto changePasswordDto = new ChangePasswordDto();
        Principal principal = mock(Principal.class);
        when(principal.getName()).thenReturn("testUser");
        ResponseDto responseDto = new ResponseDto();
        ResponseEntity<ResponseDto> expectedResponse = new ResponseEntity<>(responseDto, HttpStatus.OK);

        when(myUserService.updatePassword(changePasswordDto.getNewPassword(), principal)).thenReturn(expectedResponse);

        // Act
        ResponseEntity<ResponseDto> actualResponse = (ResponseEntity<ResponseDto>) myUserController.updatePassword(changePasswordDto, principal);

        // Assert
        assertEquals(expectedResponse, actualResponse);
        verify(myUserService, times(1)).updatePassword(changePasswordDto.getNewPassword(), principal);
    }
}
