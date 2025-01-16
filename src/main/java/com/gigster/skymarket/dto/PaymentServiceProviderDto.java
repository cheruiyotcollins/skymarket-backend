package com.gigster.skymarket.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentServiceProviderDto {
    private String serviceProviderName;
    private String shortCode;
    // ToString
    @Override
    public String toString() {
        return "PaymentServiceProviderDTO{" +
                " serviceProviderName='" + serviceProviderName + '\'' +
                ", shortCode='" + shortCode + '\'' +
                '}';
    }
}

