package com.gigster.skymarket.dto;

import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminDto {
    private Long adminId;
    private String fullName;
    private String email;
    private String contact;
    private Instant createdOn = Instant.now();
}
