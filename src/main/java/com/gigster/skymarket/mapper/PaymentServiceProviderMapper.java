package com.gigster.skymarket.mapper;

import com.gigster.skymarket.dto.PaymentServiceProviderDto;
import com.gigster.skymarket.model.PaymentServiceProvider;

public class PaymentServiceProviderMapper {

    // Convert Entity to DTO
    public static PaymentServiceProviderDto toDTO(PaymentServiceProvider entity) {
        if (entity == null) {
            return null;
        }
        return new PaymentServiceProviderDto(
                entity.getServiceProviderName(),
                entity.getShortCode()
        );
    }

    // Convert DTO to Entity
    public static PaymentServiceProvider toEntity(PaymentServiceProviderDto dto) {
        if (dto == null) {
            return null;
        }
        PaymentServiceProvider entity = new PaymentServiceProvider();
        entity.setServiceProviderName(dto.getServiceProviderName());
        entity.setShortCode(dto.getShortCode());
        return entity;
    }
}
