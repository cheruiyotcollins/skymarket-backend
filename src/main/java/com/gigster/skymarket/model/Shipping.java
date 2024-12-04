package com.gigster.skymarket.model;

import com.gigster.skymarket.enums.ShippingStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Shipping {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String address;

    private String city;

    private String state;

    private String country;

    private String postalCode;

    @Enumerated(EnumType.STRING)
    private ShippingStatus status;

    @OneToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    private String trackingNumber;
}
