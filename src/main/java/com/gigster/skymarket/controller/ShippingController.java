package com.gigster.skymarket.controller;

import com.gigster.skymarket.dto.ResponseDto;
import com.gigster.skymarket.enums.ShippingStatus;
import com.gigster.skymarket.service.ShippingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/shipping")
public class ShippingController {

    private final ShippingService shippingService;

    @Autowired
    public ShippingController(ShippingService shippingService) {
        this.shippingService = shippingService;
    }

    @PutMapping("/{shippingId}/status")
    public ResponseEntity<ResponseDto> updateShippingStatus(
            @PathVariable Long shippingId,
            @RequestParam ShippingStatus newStatus) {

        return shippingService.updateShippingStatus(shippingId, newStatus);
    }

    @GetMapping("/{shippingId}")
    public ResponseEntity<ResponseDto> getShippingDetails(@PathVariable Long shippingId) {
        return shippingService.getShippingDetails(shippingId);
    }
}
