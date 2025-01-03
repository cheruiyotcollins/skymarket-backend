package com.gigster.skymarket.mapper;

import com.gigster.skymarket.dto.CustomerDto;
import com.gigster.skymarket.model.Customer;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    CustomerDto toDto(Customer customer);

    Customer toEntity(CustomerDto customerDto);

    List<CustomerDto> toDtoList(List<Customer> customers);

    List<Customer> toEntityList(List<CustomerDto> customerDtos);
}
