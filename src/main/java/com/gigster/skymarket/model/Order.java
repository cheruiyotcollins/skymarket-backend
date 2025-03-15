package com.gigster.skymarket.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gigster.skymarket.enums.OrderStatus;
import com.gigster.skymarket.enums.PaymentProvider;
import com.gigster.skymarket.utils.DateUtils;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name="orders")
public class Order implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private long userId;

    private long productId;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<OrderProduct> orderProducts = new ArrayList<>();

    private String orderNumber;

    private double totalAmount;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    private LocalDateTime orderDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    @JsonIgnore
    private Customer customer;
    private String shippingAddress;
    private PaymentProvider paymentProvider;

    @Builder.Default
    private String createdOn= DateUtils.dateNowString();
}
