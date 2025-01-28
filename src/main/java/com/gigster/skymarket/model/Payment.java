package com.gigster.skymarket.model;

import com.gigster.skymarket.enums.PaymentStatus;
import com.gigster.skymarket.enums.PaymentProvider;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "payments")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, unique = true)
    private String paymentReference;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    private String currency;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus status;

    @Column(nullable = false)
    private Instant paymentDate;

    private Instant confirmationDate;

    private Instant updatedDate;

    @Column(nullable = false)
    private String paymentMethod;

    @Column(nullable = false)
    private String orderId;
    private String customerId;

    private String gatewayTransactionId;

    @Column(length = 500)
    private String failureReason;

    @Enumerated(EnumType.STRING)
    private PaymentProvider provider;

    private String customerEmail;

}
