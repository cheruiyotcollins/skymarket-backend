package com.gigster.skymarket.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="categories")
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String categoryName;
}
