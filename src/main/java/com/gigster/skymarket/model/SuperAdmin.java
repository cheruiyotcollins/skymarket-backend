
package com.gigster.skymarket.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

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
    private String employeeNo;
    private String contact;

    @Column(nullable = false, updatable = false)
    private Instant createdOn;

    @PrePersist
    protected void onCreate() {
        if (createdOn == null) {
            createdOn = Instant.now();
        }
    }
}
