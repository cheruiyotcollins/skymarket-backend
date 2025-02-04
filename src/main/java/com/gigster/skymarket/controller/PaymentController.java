package com.gigster.skymarket.controller;

import com.gigster.skymarket.enums.PaymentProvider;
import com.gigster.skymarket.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/v1/payments")
public class PaymentController {

    private final PaymentService paymentService;

    @Autowired
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/process")
    public ResponseEntity<?> processPayment(
            @RequestParam String paymentReference,
            @RequestParam BigDecimal amount,
            @RequestParam String currency,
            @RequestParam String paymentMethod,
            @RequestParam String orderId,
            @RequestParam String customerId,
            @RequestParam PaymentProvider provider) {

        boolean success = paymentService.processPayment(paymentReference, amount, currency, paymentMethod, orderId, customerId, provider);
        if (success) {
            return ResponseEntity.ok("Payment processed successfully");
        }
        return ResponseEntity.badRequest().body("Payment processing failed");
    }

    @PatchMapping("/confirm/{paymentReference}")
    public ResponseEntity<?> confirmPayment(@PathVariable String paymentReference) {
        paymentService.confirmPayment(paymentReference);
        return ResponseEntity.ok("Payment confirmed successfully");
    }

    @PostMapping("/failure")
    public ResponseEntity<?> handlePaymentFailure(
            @RequestParam String paymentReference,
            @RequestParam String failureReason) {

        paymentService.handlePaymentFailure(paymentReference, failureReason);
        return ResponseEntity.ok("Payment failure recorded successfully");
    }
}
