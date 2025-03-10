package com.gigster.skymarket.controller;

import com.gigster.skymarket.dto.PaymentServiceProviderDto;
import com.gigster.skymarket.service.PaymentServiceProviderService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/payment-service-providers")
public class PaymentServiceProviderController {

    private final PaymentServiceProviderService service;

    @Autowired
    public PaymentServiceProviderController(PaymentServiceProviderService service) {
        this.service = service;
    }
    @PostMapping
    public ResponseEntity<PaymentServiceProviderDto> create(@RequestBody @Valid PaymentServiceProviderDto dto) {
        PaymentServiceProviderDto createdDto = service.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PaymentServiceProviderDto> update(
            @PathVariable Long id,
            @RequestBody @Valid PaymentServiceProviderDto dto) {
        PaymentServiceProviderDto updatedDto = service.update(id, dto);
        return ResponseEntity.ok(updatedDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentServiceProviderDto> getById(@PathVariable Long id) {
        PaymentServiceProviderDto dto = service.getById(id);
        return ResponseEntity.ok(dto);
    }

    @GetMapping
    public ResponseEntity<List<PaymentServiceProviderDto>> getAll() {
        List<PaymentServiceProviderDto> dtos = service.getAll();
        return ResponseEntity.ok(dtos);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

