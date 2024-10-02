package com.gigster.skymarket.model;


import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;


@Entity
@Table(name="customers")
@Data
public class Customer implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "full_name")
    private String fullName;
    @Column(name = "email")
    private String email;
    @Column(name = "phone_no")
    private String phoneNo;
    @Column(name = "gender")
    private String gender;

}
