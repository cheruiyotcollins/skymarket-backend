package com.gigster.skymarket.service.impl;

import com.gigster.skymarket.dto.ResponseDto;
import com.gigster.skymarket.enums.OrderStatus;
import com.gigster.skymarket.enums.PaymentStatus;
import com.gigster.skymarket.enums.PaymentProvider;
import com.gigster.skymarket.exception.InvalidPaymentStatusException;
import com.gigster.skymarket.exception.PaymentNotFoundException;
import com.gigster.skymarket.exception.ResourceNotFoundException;
import com.gigster.skymarket.model.Customer;
import com.gigster.skymarket.model.Order;
import com.gigster.skymarket.model.Payment;
import com.gigster.skymarket.repository.CustomerRepository;
import com.gigster.skymarket.repository.PaymentRepository;
import com.gigster.skymarket.service.NotificationService;
import com.gigster.skymarket.service.OrderService;
import com.gigster.skymarket.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;

    private final CustomerRepository customerRepository;

    private final OrderService orderService;

    private final NotificationService notificationService;

    @Override
    public boolean processPayment(String paymentReference, BigDecimal amount, String currency, String paymentMethod, String orderId, String customerId, PaymentProvider provider) {
        // Simulate payment processing (replace with your actual integration)
        boolean paymentSuccess = simulatePayment(amount, currency, paymentMethod);
        if (paymentSuccess) {
            Payment payment = createPayment(paymentReference, amount, currency, Instant.now(), paymentMethod, orderId, customerId, provider);
            paymentRepository.save(payment);
            // Send payment confirmation notification to customer (e.g., email)
            sendPaymentConfirmationNotification(payment);
            // Update order status in the order service (e.g., set to "Payment Received")
            updateOrderStatus(payment.getOrderId());
            return true;
        } else {
            return false;
        }
    }

    private boolean simulatePayment(BigDecimal amount, String currency, String paymentMethod) {
        // Replace with your integration logic to connect to a payment gateway
        // This is a simulation for now
        return Math.random() > 0.5; // Simulate random success/failure (50% chance)
    }

    private Payment createPayment(String paymentReference, BigDecimal amount, String currency, Instant paymentDate, String paymentMethod, String orderId, String customerId, PaymentProvider provider) {
        return Payment.builder()
                .paymentReference(paymentReference)
                .amount(amount)
                .currency(currency)
                .status(PaymentStatus.PENDING)
                .paymentDate(paymentDate)
                .paymentMethod(paymentMethod)
                .orderId(orderId)
                .customerId(Long.parseLong(customerId))
                .provider(provider)
                .build();
    }

    @Override
    public void confirmPayment(String paymentReference) {
        Payment payment = paymentRepository.findByPaymentReference(paymentReference);
        if (payment != null && payment.getStatus().equals(PaymentStatus.PENDING)) {
            payment.setStatus(PaymentStatus.CONFIRMED);
            payment.setConfirmationDate(Instant.now());
            paymentRepository.save(payment);
            // Send payment confirmation notification to customer (e.g., email)
            sendPaymentConfirmationNotification(payment);
        } else {
            // Handle potential errors (e.g., payment not found or already confirmed).
            if (payment == null) {

                log.error("Payment with reference {} not found.", paymentReference);

                throw new PaymentNotFoundException("Payment with reference " + paymentReference + " not found.");
            } else {

                log.error("Payment with reference {} is not in PENDING status. Current status: {}",
                        paymentReference, payment.getStatus());

                throw new InvalidPaymentStatusException("Payment with reference " + paymentReference +
                        " is not in PENDING status. Current status: " + payment.getStatus());
            }
        }
    }

    @Override
    public void handlePaymentFailure(String paymentReference, String failureReason) {
        Payment payment = paymentRepository.findByPaymentReference(paymentReference);
        if (payment != null && payment.getStatus().equals(PaymentStatus.PENDING)) {
            payment.setStatus(PaymentStatus.FAILED);
            payment.setFailureReason(failureReason);
            paymentRepository.save(payment);

            sendPaymentFailureNotification(payment);

            updateOrderStatus(payment.getOrderId());
        } else {

            if (payment == null) {
                throw new PaymentNotFoundException("Payment with reference " + paymentReference + " not found.");
            } else {
                throw new InvalidPaymentStatusException("Payment with reference " + paymentReference +
                        " is not in PENDING status. Current status: " + payment.getStatus());
            }
        }
    }

    private void sendPaymentConfirmationNotification(Payment payment) {
        Customer customer = customerRepository.findById(payment.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer", "id", payment.getCustomerId()));

        String customerEmail = customer.getEmail();

        if (customerEmail == null || customerEmail.isEmpty()) {
            log.warn("Customer email is missing for payment with reference {}", payment.getPaymentReference());
            return;
        }

        try {
            notificationService.sendPaymentConfirmationNotification(customer, payment);
            log.info("Payment confirmation notification sent to {}", customerEmail);
        } catch (Exception e) {
            log.error("Failed to send payment confirmation notification to {}", customerEmail, e);
        }
    }

    private void sendPaymentFailureNotification(Payment payment) {
        Customer customer = customerRepository.findById(payment.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer", "id", payment.getCustomerId()));

        String customerEmail = customer.getEmail();

        if (customerEmail == null || customerEmail.isEmpty()) {
            log.warn("Customer email is missing for payment with reference {}", payment.getPaymentReference());
            return;
        }

        try {
            notificationService.sendPaymentFailureNotification(customer, payment);
            log.info("Payment failure notification sent to {}", customerEmail);
        } catch (Exception e) {
            log.error("Failed to send payment failure notification to {}", customerEmail, e);
        }
    }

    private void updateOrderStatus(String orderId) {
        if (orderId == null || orderId.isEmpty()) {
            log.warn("Order ID is missing, unable to update order status.");
            return;
        }

        try {
            // Fetch order from OrderService
            Long orderIdLong = Long.parseLong(orderId);
            ResponseEntity<ResponseDto> response = orderService.getOrderById(orderIdLong);
            ResponseDto responseDto = response.getBody();

            // Extract the order from the payload
            Order order = responseDto != null ? responseDto.getPayloadAs(Order.class) : null;

            if (order != null) {
                order.setStatus(OrderStatus.PAYMENT_FAILED);

                orderService.saveOrder(order);
                log.info("Order status updated for order ID {}", orderId);
            } else {
                log.warn("Order with ID {} not found.", orderId);
            }
        } catch (Exception e) {
            log.error("Failed to update order status for order ID {}", orderId, e);
        }
    }

}
