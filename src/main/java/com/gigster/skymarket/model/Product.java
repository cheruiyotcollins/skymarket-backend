package com.gigster.skymarket.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

import java.io.Serializable;

@Entity
@Table(name = "products")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Product implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, unique = true)
    private String productName;
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "category_id")
    private Category category;

    private String manufacturer;

    @Column(length = 1000)
    private String description;

    private String imageUrl;

    @Column(nullable = false)
    @PositiveOrZero
    private double price;

    @Column(nullable = false)
    private int stock;
    private double rating;
    private int count;

}
