package com.gigster.skymarket.service.impl;

import com.gigster.skymarket.model.Customer;
import com.gigster.skymarket.model.Order;
import com.gigster.skymarket.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private JavaMailSender mailSender;

    @Async("asyncExecutor")
    @Override
    public void sendMail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
    }

    // Method to send a pre-formatted order confirmation email
    public void sendOrderConfirmationEmail(Customer customer, Order order) {
        String sendTo = customer.getEmail();
        String subject = "Order Confirmation";
        String emailBody = "Dear " + customer.getFullName() + ",\n\n" +
                "Thank you for your recent order. We are pleased to confirm that your order has been received and is now being processed.\n" +
                "Order Details:\n" +
                "Order Number: " + order.getOrderNumber() + "\n" +
                "Order Date: " + order.getOrderDate() + "\n" +
                "Total Amount: KES " + order.getTotalAmount() + "\n\n" +
                "We will notify you once your order has been shipped. You can track the status of your order by logging into your account.\n\n" +
                "If you have any questions, please feel free to contact our customer service team.\n\n" +
                "Regards,\n" +
                "SkyMarket Online Store Team";

        sendMail(sendTo, subject, emailBody);
    }
}
