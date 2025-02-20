package com.gigster.skymarket.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Admin")
@Builder
public class Admin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long adminId;
    private String fullName;
    private String email;
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
