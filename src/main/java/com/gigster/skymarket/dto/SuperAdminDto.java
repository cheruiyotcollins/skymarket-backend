package com.gigster.skymarket.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SuperAdminDto {

    private Long superAdminId;
    private String fullName;
    private String email;
    private String employeeNo;
    private String contact;
    private Instant createdOn = Instant.now();
}
