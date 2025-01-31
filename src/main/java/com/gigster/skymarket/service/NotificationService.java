package com.gigster.skymarket.service;

import com.gigster.skymarket.model.Customer;
import com.gigster.skymarket.model.Order;
import com.gigster.skymarket.model.Payment;

public interface NotificationService {

    // Method to send an email
    void sendMail(String to, String subject, String body);

    void sendOrderConfirmationEmail(Customer customer, Order order);

    void sendPaymentFailureNotification(Customer customer, Payment payment);

    void sendPaymentConfirmationNotification(Customer customer, Payment payment);

//    TODO: To be implemented in future(current option is Twilio).
//    Method to send SMS
//    void sendSms(String to, String message);
}