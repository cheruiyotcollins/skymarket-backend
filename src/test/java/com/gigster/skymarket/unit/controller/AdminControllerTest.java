package com.gigster.skymarket.unit.controller;

import com.gigster.skymarket.controller.AdminController;
import com.gigster.skymarket.dto.AdminDto;
import com.gigster.skymarket.dto.ExtendedResDto;
import com.gigster.skymarket.dto.ResponseDto;
import com.gigster.skymarket.service.AdminService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class AdminControllerTest {

    @Mock
    private AdminService adminService;

    @InjectMocks
    private AdminController adminController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addAdmin_ShouldReturnResponseEntity() {
        // Arrange
        AdminDto newAdmin = new AdminDto();
        ResponseDto responseDto = new ResponseDto();
        ResponseEntity<ResponseDto> expectedResponse = new ResponseEntity<>(responseDto, HttpStatus.CREATED);

        when(adminService.addAdmin(newAdmin)).thenReturn(expectedResponse);

        // Act
        ResponseEntity<ResponseDto> actualResponse = adminController.addAdmin(newAdmin);

        // Assert
        assertEquals(expectedResponse, actualResponse);
        verify(adminService, times(1)).addAdmin(newAdmin);
    }

    @Test
    void getAdminById_ShouldReturnAdmin() {
        // Arrange
        Long adminId = 1L;
        ExtendedResDto extendedResDto = new ExtendedResDto();

        when(adminService.getAdminById(adminId)).thenReturn(extendedResDto);

        // Act
        ExtendedResDto actualResponse = adminController.getAdminById(adminId);

        // Assert
        assertEquals(extendedResDto, actualResponse);
        verify(adminService, times(1)).getAdminById(adminId);
    }

    @Test
    void getAllAdmins_ShouldReturnPaginatedAdmins() {
        // Arrange
        int page = 0;
        int size = 20;
        String sort = "id,asc";
        ExtendedResDto extendedResDto = new ExtendedResDto();

        when(adminService.getAllAdmins(page, size, sort)).thenReturn(extendedResDto);

        // Act
        ResponseEntity<ExtendedResDto> actualResponse = adminController.getAllAdmins(page, size, sort);

        // Assert
        assertEquals(HttpStatus.OK, actualResponse.getStatusCode());
        assertEquals(extendedResDto, actualResponse.getBody());
        verify(adminService, times(1)).getAllAdmins(page, size, sort);
    }

    @Test
    void updateAdminById_ShouldReturnUpdatedAdmin() {
        // Arrange
        AdminDto adminDto = new AdminDto();
        ExtendedResDto extendedResDto = new ExtendedResDto();

        when(adminService.updateAdminById(adminDto)).thenReturn(extendedResDto);

        // Act
        ExtendedResDto actualResponse = adminController.updateAdminById(adminDto);

        // Assert
        assertEquals(extendedResDto, actualResponse);
        verify(adminService, times(1)).updateAdminById(adminDto);
    }

    @Test
    void deleteAdminById_ShouldReturnResponseEntity() {
        // Arrange
        Long adminId = 1L;
        ResponseDto responseDto = new ResponseDto();
        ResponseEntity<ResponseDto> expectedResponse = new ResponseEntity<>(responseDto, HttpStatus.NO_CONTENT);

        when(adminService.deleteAdminById(adminId)).thenReturn(expectedResponse);

        // Act
        ResponseEntity<ResponseDto> actualResponse = adminController.deleteAdminById(adminId);

        // Assert
        assertEquals(expectedResponse, actualResponse);
        verify(adminService, times(1)).deleteAdminById(adminId);
    }
}
