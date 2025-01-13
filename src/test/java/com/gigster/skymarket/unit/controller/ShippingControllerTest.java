package com.gigster.skymarket.unit.controller;

import com.gigster.skymarket.controller.ShippingController;
import com.gigster.skymarket.dto.ResponseDto;
import com.gigster.skymarket.enums.ShippingStatus;
import com.gigster.skymarket.service.ShippingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ShippingControllerTest {

    @Mock
    private ShippingService shippingService;

    @InjectMocks
    private ShippingController shippingController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void updateShippingStatus_ShouldReturnUpdatedStatus() {
        // Arrange
        Long shippingId = 1L;
        ShippingStatus newStatus = ShippingStatus.DELIVERED;
        ResponseDto responseDto = new ResponseDto();
        ResponseEntity<ResponseDto> expectedResponse = new ResponseEntity<>(responseDto, HttpStatus.OK);

        when(shippingService.updateShippingStatus(shippingId, newStatus)).thenReturn(expectedResponse);

        // Act
        ResponseEntity<ResponseDto> actualResponse = shippingController.updateShippingStatus(shippingId, newStatus);

        // Assert
        assertEquals(expectedResponse, actualResponse);
        verify(shippingService, times(1)).updateShippingStatus(shippingId, newStatus);
    }

    @Test
    void getShippingDetails_ShouldReturnShippingDetails() {
        // Arrange
        Long shippingId = 1L;
        ResponseDto responseDto = new ResponseDto();
        ResponseEntity<ResponseDto> expectedResponse = new ResponseEntity<>(responseDto, HttpStatus.OK);

        when(shippingService.getShippingDetails(shippingId)).thenReturn(expectedResponse);

        // Act
        ResponseEntity<ResponseDto> actualResponse = shippingController.getShippingDetails(shippingId);

        // Assert
        assertEquals(expectedResponse, actualResponse);
        verify(shippingService, times(1)).getShippingDetails(shippingId);
    }
}
