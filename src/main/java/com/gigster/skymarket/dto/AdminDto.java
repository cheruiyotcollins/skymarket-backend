package com.gigster.skymarket.dto;

import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminDto {
    private Long adminId;
    private  String fullName;
    private String email;
    private String contact;
    @Temporal(TemporalType.TIMESTAMP)
    private String CreatedOn;
}