package com.gigster.skymarket.service;


import com.gigster.skymarket.dto.PaymentServiceProviderDto;

import java.util.List;

public interface PaymentServiceProviderService {
    PaymentServiceProviderDto create(PaymentServiceProviderDto paymentServiceProvider);
    PaymentServiceProviderDto update(long id, PaymentServiceProviderDto paymentServiceProvider);
    PaymentServiceProviderDto getById(long id);
    List<PaymentServiceProviderDto> getAll();
    void deleteById(long id);
}
