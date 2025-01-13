package com.gigster.skymarket.model;

import com.gigster.skymarket.utils.DateUtils;
import jakarta.persistence.*;
import lombok.*;

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
    private  String adminName;
    private String email;
    private String contact;
    @Builder.Default
    private String createdOn= DateUtils.dateNowString();
}
