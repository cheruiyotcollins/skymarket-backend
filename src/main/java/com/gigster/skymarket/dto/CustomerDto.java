package com.gigster.skymarket.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDto {
    private Long customerId;
    private String fullName;
    private String email;
    private String phoneNo;
    private String gender;
    
}