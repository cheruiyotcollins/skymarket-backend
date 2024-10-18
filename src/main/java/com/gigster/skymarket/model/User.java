package com.gigster.skymarket.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Entity
@Table(name="users")
@Data
@Builder
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String fullName;
    private String username;
    private String email;
    private String phoneNo;
    private String password;
}
