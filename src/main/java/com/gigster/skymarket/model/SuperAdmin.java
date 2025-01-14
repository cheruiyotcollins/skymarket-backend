
package com.gigster.skymarket.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SuperAdmin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long superAdminId;
    private String fullName;
    private String email;
    private String createdOn;
    private String employeeNo;
    private String contact;
}
