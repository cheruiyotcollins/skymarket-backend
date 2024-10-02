package com.gigster.skymarket.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name="products")
@Data
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String productName;
    private String category;
    private String manufacturer;
    private double price;
    private int stock;
}
