package com.gigster.skymarket.service;

public interface NotificationService {

    void sendMail(String to, String subject, String body);

    // todo add sms notification
}



