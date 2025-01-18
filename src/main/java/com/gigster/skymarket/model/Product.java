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
//todo agree on using either id or product id
    @Column(nullable = true, unique = true)
    private Long productId;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "category_id")
    private Category category;

    private String manufacturer;

    @Column(length = 1000) // Set a limit on description length
    private String description;

    private String imageUrl;

    @Column(nullable = false)
    @PositiveOrZero
    private double price;

    @Column(nullable = false)
    private int stock;
    //todo set rating and count to 0 when a product is added
    private double rating;
    private int count;

}
