package com.gigster.skymarket.service;

import com.gigster.skymarket.dto.ResponseDto;
import com.gigster.skymarket.enums.ShippingStatus;
import org.springframework.http.ResponseEntity;

public interface ShippingService {

    ResponseEntity<ResponseDto> updateShippingStatus(Long shippingId, ShippingStatus newStatus);

    ResponseEntity<ResponseDto> getShippingDetails(Long shippingId);
}
