package com.gigster.skymarket.mapper;

import com.gigster.skymarket.dto.PaymentServiceProviderDto;
import com.gigster.skymarket.model.PaymentServiceProvider;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PaymentServiceProviderMapper {

    @Mapping(source = "serviceProviderName", target = "serviceProviderName", qualifiedByName = "trimString")
    @Mapping(source = "shortCode", target = "shortCode", qualifiedByName = "trimString")
    PaymentServiceProviderDto toDto(PaymentServiceProvider entity);

    @Mapping(source = "serviceProviderName", target = "serviceProviderName", qualifiedByName = "trimString")
    @Mapping(source = "shortCode", target = "shortCode", qualifiedByName = "trimString")
    PaymentServiceProvider toEntity(PaymentServiceProviderDto dto);

    List<PaymentServiceProviderDto> toDtoList(List<PaymentServiceProvider> entities);

    List<PaymentServiceProvider> toEntityList(List<PaymentServiceProviderDto> dtos);

    // Custom method to trim Strings and handle null values
    @Named("trimString")
    static String trimString(String value) {
        return value != null ? value.trim() : null;
    }
}
