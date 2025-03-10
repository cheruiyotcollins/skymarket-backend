package com.gigster.skymarket.unit.controller;

import com.gigster.skymarket.controller.SuperAdminController;
import com.gigster.skymarket.dto.ExtendedResDto;
import com.gigster.skymarket.dto.ResponseDto;
import com.gigster.skymarket.dto.SuperAdminDto;
import com.gigster.skymarket.service.SuperAdminService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class SuperAdminControllerTest {

    @Mock
    private SuperAdminService superAdminService;

    @InjectMocks
    private SuperAdminController superAdminController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addSuperAdmin_ShouldReturnResponseEntity() {
        // Arrange
        SuperAdminDto newSuperAdmin = new SuperAdminDto();
        ResponseDto responseDto = new ResponseDto();
        ResponseEntity<ResponseDto> expectedResponse = new ResponseEntity<>(responseDto, HttpStatus.CREATED);

        when(superAdminService.addSuperAdmin(newSuperAdmin)).thenReturn(expectedResponse);

        // Act
        ResponseEntity<ResponseDto> actualResponse = superAdminController.addSuperAdmin(newSuperAdmin);

        // Assert
        assertEquals(expectedResponse, actualResponse);
        verify(superAdminService, times(1)).addSuperAdmin(newSuperAdmin);
    }

    @Test
    void getSuperAdminById_ShouldReturnSuperAdmin() {
        // Arrange
        Long id = 1L;
        ExtendedResDto extendedResDto = new ExtendedResDto();

        when(superAdminService.getSuperAdminById(id)).thenReturn(extendedResDto);

        // Act
        ExtendedResDto actualResponse = superAdminController.getSuperAdminById(id);

        // Assert
        assertEquals(extendedResDto, actualResponse);
        verify(superAdminService, times(1)).getSuperAdminById(id);
    }

    @Test
    void getAllSuperAdmins_ShouldReturnAllSuperAdmins() {
        // Arrange
        ExtendedResDto extendedResDto = new ExtendedResDto();

        when(superAdminService.getAllSuperAdmins()).thenReturn(extendedResDto);

        // Act
        ExtendedResDto actualResponse = superAdminController.getAllSuperAdmins();

        // Assert
        assertEquals(extendedResDto, actualResponse);
        verify(superAdminService, times(1)).getAllSuperAdmins();
    }

    @Test
    void updateSuperAdminById_ShouldReturnUpdatedSuperAdmin() {
        // Arrange
        SuperAdminDto superAdminDto = new SuperAdminDto();
        ExtendedResDto extendedResDto = new ExtendedResDto();

        when(superAdminService.updateSuperAdminById(superAdminDto)).thenReturn(extendedResDto);

        // Act
        ExtendedResDto actualResponse = superAdminController.updateAdminById(superAdminDto);

        // Assert
        assertEquals(extendedResDto, actualResponse);
        verify(superAdminService, times(1)).updateSuperAdminById(superAdminDto);
    }

    @Test
    void deleteSuperAdminById_ShouldReturnResponseEntity() {
        // Arrange
        Long id = 1L;
        ResponseDto responseDto = new ResponseDto();
        ResponseEntity<ResponseDto> expectedResponse = new ResponseEntity<>(responseDto, HttpStatus.NO_CONTENT);

        when(superAdminService.deleteSuperAdminById(id)).thenReturn(expectedResponse);

        // Act
        ResponseEntity<ResponseDto> actualResponse = superAdminController.deleteSuperAdminById(id);

        // Assert
        assertEquals(expectedResponse, actualResponse);
        verify(superAdminService, times(1)).deleteSuperAdminById(id);
    }
}
