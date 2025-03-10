//package com.gigster.skymarket.unit.controller;
//
//import com.gigster.skymarket.controller.UserController;
//import com.gigster.skymarket.dto.*;
//import com.gigster.skymarket.service.UserService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.domain.Sort;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.Authentication;
//
//import java.security.Principal;
//import java.util.Objects;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.Mockito.*;
//
//class UserControllerTest {
//
//    @Mock
//    private UserService userService;
//
//    @InjectMocks
//    private UserController userController;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    void register_ShouldReturnResponseEntity() {
//        // Arrange
//        SignUpRequestDto signUpRequestDto = new SignUpRequestDto();
//        ResponseDto responseDto = new ResponseDto();
//        ResponseEntity<ResponseDto> expectedResponse = new ResponseEntity<>(responseDto, HttpStatus.CREATED);
//
//        when(userService.register(signUpRequestDto)).thenReturn(expectedResponse);
//
//        // Act
//        ResponseEntity<ResponseDto> actualResponse = userController.register(signUpRequestDto);
//
//        // Assert
//        assertEquals(expectedResponse, actualResponse);
//        verify(userService, times(1)).register(signUpRequestDto);
//    }
//
//    @Test
//    void login_ShouldReturnResponseEntity() {
//        // Arrange
//        LoginDto loginDto = new LoginDto();
//        LoginResponseDto loginResponseDto = new LoginResponseDto();
//        loginResponseDto.setAccessToken("token");
//        JWTAuthResponseDto jwtAuthResponseDto = new JWTAuthResponseDto();
//        jwtAuthResponseDto.setAccessToken("token");
//
//        when(userService.login(loginDto)).thenReturn(loginResponseDto);
//
//        // Act
//        ResponseEntity<?> actualResponse = userController.login(loginDto);
//
//        // Assert - Compare the access token values instead of whole ResponseEntity.
//        assertEquals(jwtAuthResponseDto.getAccessToken(), ((JWTAuthResponseDto) Objects.requireNonNull(actualResponse.getBody())).getAccessToken());
//        verify(userService, times(1)).login(loginDto);
//    }
//
//    @Test
//    void updateUser_ShouldReturnResponseEntity() {
//        // Arrange
//        SignUpRequestDto signUpRequestDto = new SignUpRequestDto();
//        ResponseDto responseDto = new ResponseDto();
//        ResponseEntity<ResponseDto> expectedResponse = new ResponseEntity<>(responseDto, HttpStatus.CREATED);
//
//        when(userService.register(signUpRequestDto)).thenReturn(expectedResponse);
//
//        // Act
//        ResponseEntity<ResponseDto> actualResponse = (ResponseEntity<ResponseDto>) userController.updateUser(signUpRequestDto);
//
//        // Assert
//        assertEquals(expectedResponse, actualResponse);
//        verify(userService, times(1)).register(signUpRequestDto);
//    }
//
//    @Test
//    void findUserById_ShouldReturnResponseEntity() {
//        // Arrange
//        long userId = 1L;
//        ResponseDto responseDto = new ResponseDto();
//        ResponseEntity<ResponseDto> expectedResponse = new ResponseEntity<>(responseDto, HttpStatus.FOUND);
//
//        when(userService.findUserById(userId)).thenReturn(expectedResponse);
//
//        // Act
//        ResponseEntity<ResponseDto> actualResponse = (ResponseEntity<ResponseDto>) userController.findUserById(userId);
//
//        // Assert
//        assertEquals(expectedResponse, actualResponse);
//        verify(userService, times(1)).findUserById(userId);
//    }
//
//    @Test
//    void getAllUsers_ShouldReturnResponseEntity() {
//        // Arrange
//        int page = 0;
//        int size = 10;
//        String sort = "id,asc";
//        Pageable pageable = PageRequest.of(page, size, Sort.by(sort.split(",")));
//        ResponseDto responseDto = new ResponseDto();
//        ResponseEntity<ResponseDto> expectedResponse = new ResponseEntity<>(responseDto, HttpStatus.FOUND);
//
//        when(userService.getAllUsers(pageable)).thenReturn(expectedResponse);
//
//        // Act
//        ResponseEntity<ResponseDto> actualResponse = userController.getAllUsers(page, size, sort);
//
//        // Assert
//        assertEquals(expectedResponse, actualResponse);
//        verify(userService, times(1)).getAllUsers(pageable);
//    }
//
//    @Test
//    void deleteById_ShouldReturnResponseEntity() {
//        // Arrange
//        long userId = 1L;
//        ResponseDto responseDto = new ResponseDto();
//        ResponseEntity<ResponseDto> expectedResponse = new ResponseEntity<>(responseDto, HttpStatus.OK);
//
//        when(userService.deleteById(userId)).thenReturn(expectedResponse);
//
//        // Act
//        ResponseEntity<ResponseDto> actualResponse = (ResponseEntity<ResponseDto>) userController.deleteById(userId);
//
//        // Assert
//        assertEquals(expectedResponse, actualResponse);
//        verify(userService, times(1)).deleteById(userId);
//    }
//
//    @Test
//    void addRole_ShouldReturnResponseEntity() {
//        // Arrange
//        AddRoleRequestDto addRoleRequestDto = new AddRoleRequestDto();
//        ResponseDto responseDto = new ResponseDto();
//        ResponseEntity<ResponseDto> expectedResponse = new ResponseEntity<>(responseDto, HttpStatus.CREATED);
//
//        when(userService.addRole(addRoleRequestDto)).thenReturn(expectedResponse);
//
//        // Act
//        ResponseEntity<ResponseDto> actualResponse = (ResponseEntity<ResponseDto>) userController.addRole(addRoleRequestDto);
//
//        // Assert
//        assertEquals(expectedResponse, actualResponse);
//        verify(userService, times(1)).addRole(addRoleRequestDto);
//    }
//
//    @Test
//    void getCurrentUser_ShouldReturnResponseEntity() {
//        // Arrange
//        Authentication authentication = mock(Authentication.class);
//        String username = "testUser";
//        when(authentication.getName()).thenReturn(username);
//        ResponseDto responseDto = new ResponseDto();
//        ResponseEntity<ResponseDto> expectedResponse = new ResponseEntity<>(responseDto, HttpStatus.CREATED);
//
//        when(userService.getCurrentUser(username)).thenReturn(expectedResponse);
//
//        // Act
//        ResponseEntity<ResponseDto> actualResponse = (ResponseEntity<ResponseDto>) userController.getCurrentUser(authentication);
//
//        // Assert
//        assertEquals(expectedResponse, actualResponse);
//        verify(userService, times(1)).getCurrentUser(username);
//    }
//
//    @Test
//    void updatePassword_ShouldReturnResponseEntity() {
//        // Arrange
//        ChangePasswordDto changePasswordDto = new ChangePasswordDto();
//        Principal principal = mock(Principal.class);
//        when(principal.getName()).thenReturn("testUser");
//        ResponseDto responseDto = new ResponseDto();
//        ResponseEntity<ResponseDto> expectedResponse = new ResponseEntity<>(responseDto, HttpStatus.OK);
//
//        when(userService.updatePassword(changePasswordDto.getNewPassword(), principal)).thenReturn(expectedResponse);
//
//        // Act
//        ResponseEntity<ResponseDto> actualResponse = (ResponseEntity<ResponseDto>) userController.updatePassword(changePasswordDto, principal);
//
//        // Assert
//        assertEquals(expectedResponse, actualResponse);
//        verify(userService, times(1)).updatePassword(changePasswordDto.getNewPassword(), principal);
//    }
//}
