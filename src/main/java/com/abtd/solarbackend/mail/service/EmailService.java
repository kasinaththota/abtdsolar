package com.abtd.solarbackend.mail.service;

public interface EmailService {

    void sendEmail(
            String to,
            String subject,
            String body);

    void sendEmailWithAttachment(
            String to,
            String subject,
            String body,
            byte[] attachment,
            String fileName);
}