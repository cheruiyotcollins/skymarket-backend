package com.gigster.skymarket.service;


import com.gigster.skymarket.dto.PaymentServiceProviderDto;
import com.gigster.skymarket.dto.ResponseDto;
import org.springframework.http.ResponseEntity;

public interface PaymentServiceProviderService {
    PaymentServiceProviderDto create(PaymentServiceProviderDto paymentServiceProvider);

    PaymentServiceProviderDto update(long id, PaymentServiceProviderDto paymentServiceProvider);

    PaymentServiceProviderDto getById(long id);

    ResponseEntity<ResponseDto> getAllPSP(int page, int size, String sort);

    void deleteById(long id);
}
