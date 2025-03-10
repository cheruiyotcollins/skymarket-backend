package com.gigster.skymarket.unit.controller;

import com.gigster.skymarket.controller.PaymentServiceProviderController;
import com.gigster.skymarket.dto.PaymentServiceProviderDto;
import com.gigster.skymarket.service.PaymentServiceProviderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class PaymentServiceProviderControllerTest {

    @Mock
    private PaymentServiceProviderService service;

    @InjectMocks
    private PaymentServiceProviderController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void create_ShouldReturnCreatedPaymentServiceProviderDto() {
        // Arrange
        PaymentServiceProviderDto dto = new PaymentServiceProviderDto();
        dto.setServiceProviderName("Provider1"); // Set other fields as needed
        PaymentServiceProviderDto createdDto = new PaymentServiceProviderDto();
        createdDto.setServiceProviderName("Provider1"); // Set other fields as needed

        when(service.create(dto)).thenReturn(createdDto);

        // Act
        ResponseEntity<PaymentServiceProviderDto> response = controller.create(dto);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(createdDto, response.getBody());
        verify(service, times(1)).create(dto);
    }

    @Test
    void update_ShouldReturnUpdatedPaymentServiceProviderDto() {
        // Arrange
        Long id = 1L;
        PaymentServiceProviderDto dto = new PaymentServiceProviderDto();
        dto.setServiceProviderName("UpdatedProvider"); // Set other fields as needed
        PaymentServiceProviderDto updatedDto = new PaymentServiceProviderDto();
        updatedDto.setServiceProviderName("UpdatedProvider"); // Set other fields as needed

        when(service.update(id, dto)).thenReturn(updatedDto);

        // Act
        ResponseEntity<PaymentServiceProviderDto> response = controller.update(id, dto);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedDto, response.getBody());
        verify(service, times(1)).update(id, dto);
    }

    @Test
    void getById_ShouldReturnPaymentServiceProviderDto() {
        // Arrange
        Long id = 1L;
        PaymentServiceProviderDto dto = new PaymentServiceProviderDto();
        dto.setServiceProviderName("Provider1"); // Set other fields as needed

        when(service.getById(id)).thenReturn(dto);

        // Act
        ResponseEntity<PaymentServiceProviderDto> response = controller.getById(id);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(dto, response.getBody());
        verify(service, times(1)).getById(id);
    }

    @Test
    void getAll_ShouldReturnListOfPaymentServiceProviderDtos() {
        // Arrange
        PaymentServiceProviderDto dto1 = new PaymentServiceProviderDto();
        dto1.setServiceProviderName("Provider1"); // Set other fields as needed
        PaymentServiceProviderDto dto2 = new PaymentServiceProviderDto();
        dto2.setServiceProviderName("Provider2"); // Set other fields as needed
        List<PaymentServiceProviderDto> dtos = List.of(dto1, dto2);

        when(service.getAll()).thenReturn(dtos);

        // Act
        ResponseEntity<List<PaymentServiceProviderDto>> response = controller.getAll();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(dtos, response.getBody());
        verify(service, times(1)).getAll();
    }

    @Test
    void deleteById_ShouldReturnNoContent() {
        // Arrange
        Long id = 1L;

        // Act
        ResponseEntity<Void> response = controller.deleteById(id);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(service, times(1)).deleteById(id);
    }
}
