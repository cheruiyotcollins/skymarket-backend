package com.gigster.skymarket.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CustomerResponseDto {
    private String fullName;
    private String email;
    private String phoneNo;
    private String gender;
}

