package com.gigster.skymarket.service;

public interface NotificationService {

    // Method to send an email
    void sendMail(String to, String subject, String body);

//    TODO: To be implemented in future(current option is Twilio).
//    Method to send SMS
//    void sendSms(String to, String message);
}
