package com.gigster.skymarket.dto;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NewCustomerDto {
    private String fullName;
    private String email;
    private String phoneNo;
    private String gender;
}
