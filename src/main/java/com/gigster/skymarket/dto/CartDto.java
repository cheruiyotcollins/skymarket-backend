package com.gigster.skymarket.dto;

import com.gigster.skymarket.model.Customer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartDto {
//    long productId;
//    int quantity;
    Customer customer;
}
