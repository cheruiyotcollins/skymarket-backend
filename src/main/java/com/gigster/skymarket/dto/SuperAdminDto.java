package com.gigster.skymarket.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SuperAdminDto {

    private Long superAdminId;
    private String fullName;
    private String email;
    private String employeeNo;
    private String contact;
}
