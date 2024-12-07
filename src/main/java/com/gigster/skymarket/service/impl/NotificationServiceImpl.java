package com.gigster.skymarket.service.impl;

import com.gigster.skymarket.model.Customer;
import com.gigster.skymarket.model.Order;
import com.gigster.skymarket.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class NotificationServiceImpl implements NotificationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationServiceImpl.class);

    @Autowired
    private JavaMailSender mailSender;

    // Synchronous logging/storing method
    private void logEmailDetails(String to, String subject, String body) {
        // Log email details for audit/debugging purposes
        LOGGER.info("Logging email details: To: {}, Subject: {}, Body: {}", to, subject, body);

        // Optionally, store the email details in the database for retry or auditing
        // Example:
        // EmailLog emailLog = new EmailLog(to, subject, body, LocalDateTime.now());
        // emailLogRepository.save(emailLog);
    }

    @Async("asyncExecutor")
    @Override
    public void sendMail(String to, String subject, String body) {
        try {
            if (to == null || to.isBlank()) {
                throw new IllegalArgumentException("Email address is null or empty.");
            }
            if (subject == null || subject.isBlank()) {
                throw new IllegalArgumentException("Email subject is null or empty.");
            }
            if (body == null || body.isBlank()) {
                throw new IllegalArgumentException("Email body is null or empty.");
            }

            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);
            mailSender.send(message);

            LOGGER.info("Email successfully sent to {}", to);
        } catch (Exception e) {
            LOGGER.error("Failed to send email to {}. Error: {}", to, e.getMessage());
            // Optionally, update the email log with the failure status in the database.
        }
    }

    public void sendOrderConfirmationEmail(Customer customer, Order order) {
        String sendTo = customer.getEmail();
        String subject = "Order Confirmation";

        String emailBody = String.format(
                "Dear %s,\n\nThank you for your recent order. We are pleased to confirm that your order has been received and is now being processed.\n\n"
                        + "Order Details:\nOrder Number: %s\nOrder Date: %s\nTotal Amount: KES %.2f\n\n"
                        + "We will notify you once your order has been shipped. You can track the status of your order by logging into your account.\n\n"
                        + "If you have any questions, please feel free to contact our customer service team.\n\n"
                        + "Regards,\nSkyMarket Online Store Team",
                customer.getFullName(),
                order.getOrderNumber(),
                order.getOrderDate(),
                order.getTotalAmount()
        );

        // Log the email details synchronously
        logEmailDetails(sendTo, subject, emailBody);

        // Asynchronous email sending
        sendOrderConfirmationEmailAsync(sendTo, subject, emailBody);
    }

    @Async("asyncExecutor")
    public void sendOrderConfirmationEmailAsync(String to, String subject, String body) {
        sendMail(to, subject, body);
    }

}
