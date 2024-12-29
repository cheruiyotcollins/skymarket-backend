package com.gigster.skymarket.model;

import com.gigster.skymarket.enums.RoleName;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.NaturalId;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @NaturalId
    @Column(length = 60, unique = true)
    private RoleName name;
}
