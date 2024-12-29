package com.gigster.skymarket.service.impl;

import com.gigster.skymarket.dto.ResponseDto;
import com.gigster.skymarket.enums.ShippingStatus;
import com.gigster.skymarket.exception.ShippingNotFoundException;
import com.gigster.skymarket.model.Shipping;
import com.gigster.skymarket.repository.ShippingRepository;
import com.gigster.skymarket.service.ShippingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ShippingServiceImpl implements ShippingService {

    private final ShippingRepository shippingRepository;

    @Autowired
    public ShippingServiceImpl(ShippingRepository shippingRepository) {
        this.shippingRepository = shippingRepository;
    }

    @Override
    public ResponseEntity<ResponseDto> updateShippingStatus(Long shippingId, ShippingStatus newStatus) {
        Optional<Shipping> optionalShipping = shippingRepository.findById(shippingId);
        if (optionalShipping.isPresent()) {
            Shipping shipping = optionalShipping.get();
            ShippingStatus currentStatus = shipping.getStatus();

            // Logic to prevent invalid status transitions.
            if (isValidStatusTransition(currentStatus, newStatus)) {
                shipping.setStatus(newStatus);
                shippingRepository.save(shipping);
                return new ResponseEntity<>(
                        ResponseDto.builder()
                                .status(HttpStatus.OK)
                                .description("Shipping status updated successfully.")
                                .payload(shipping)
                                .build(),
                        HttpStatus.OK
                );
            } else {
                return new ResponseEntity<>(
                        ResponseDto.builder()
                                .status(HttpStatus.CONFLICT)
                                .description("Invalid status transition.")
                                .payload(null)
                                .build(),
                        HttpStatus.CONFLICT
                );
            }
        } else {
            throw new ShippingNotFoundException("Shipping with ID " + shippingId + " not found");
        }
    }

    @Override
    public ResponseEntity<ResponseDto> getShippingDetails(Long shippingId) {
        Shipping shipping = shippingRepository.findById(shippingId)
                .orElseThrow(() -> new ShippingNotFoundException("Shipping with ID " + shippingId + " not found"));

        return new ResponseEntity<>(
                ResponseDto.builder()
                        .status(HttpStatus.OK)
                        .description("Shipping details fetched successfully")
                        .payload(shipping)
                        .build(),
                HttpStatus.OK
        );
    }

    // Method to validate status transitions.
    private boolean isValidStatusTransition(ShippingStatus currentStatus, ShippingStatus newStatus) {
        return switch (currentStatus) {
            case PENDING -> newStatus == ShippingStatus.PROCESSING || newStatus == ShippingStatus.CANCELLED;
            case PROCESSING -> newStatus == ShippingStatus.IN_TRANSIT || newStatus == ShippingStatus.CANCELLED;
            case IN_TRANSIT -> newStatus == ShippingStatus.OUT_FOR_DELIVERY || newStatus == ShippingStatus.RETURNED;
            case OUT_FOR_DELIVERY -> newStatus == ShippingStatus.DELIVERED || newStatus == ShippingStatus.RETURNED;
            case DELIVERED, RETURNED, CANCELLED -> false;  // Cannot change status after delivery, return, or cancellation
        };
    }

}