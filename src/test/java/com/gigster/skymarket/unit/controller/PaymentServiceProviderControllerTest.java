package com.gigster.skymarket.unit.controller;

import com.gigster.skymarket.controller.PaymentServiceProviderController;
import com.gigster.skymarket.dto.PaymentServiceProviderDto;
import com.gigster.skymarket.dto.ResponseDto;
import com.gigster.skymarket.service.PaymentServiceProviderService;
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

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
        dto.setServiceProviderName("Provider1");
        PaymentServiceProviderDto createdDto = new PaymentServiceProviderDto();
        createdDto.setServiceProviderName("Provider1");

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
        dto.setServiceProviderName("UpdatedProvider");
        PaymentServiceProviderDto updatedDto = new PaymentServiceProviderDto();
        updatedDto.setServiceProviderName("UpdatedProvider");

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
        dto.setServiceProviderName("Provider1");

        when(service.getById(id)).thenReturn(dto);

        // Act
        ResponseEntity<PaymentServiceProviderDto> response = controller.getById(id);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(dto, response.getBody());
        verify(service, times(1)).getById(id);
    }

    @Test
    void getAll_ShouldReturnPaginatedListOfPaymentServiceProviderDtos() {
        // Arrange
        int page = 0;
        int size = 10;
        String sort = "id,asc";
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort.split(",")));

        PaymentServiceProviderDto dto1 = new PaymentServiceProviderDto();
        dto1.setServiceProviderName("Provider1");

        PaymentServiceProviderDto dto2 = new PaymentServiceProviderDto();
        dto2.setServiceProviderName("Provider2");

        List<PaymentServiceProviderDto> dtos = List.of(dto1, dto2);

        ResponseDto responseDto = ResponseDto.builder()
                .status(HttpStatus.OK)
                .description("List of All Payment Service Providers.")
                .payload(dtos)
                .totalPages(1)
                .totalElements((long) dtos.size())
                .currentPage(page)
                .pageSize(size)
                .build();

        ResponseEntity<ResponseDto> expectedResponse = ResponseEntity.ok(responseDto);

        when(service.getAllPSP(page, size, sort)).thenReturn(expectedResponse);

        // Act
        ResponseEntity<ResponseDto> response = controller.getAllPSP(page, size, sort);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(responseDto, response.getBody());
        verify(service, times(1)).getAllPSP(page, size, sort);
    }

    @Test
    void deleteById_ShouldReturnNoContent() {
        // Arrange
        long id = 1L;

        // Act
        ResponseEntity<Void> response = controller.deleteById(id);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(service, times(1)).deleteById(id);
    }
}
