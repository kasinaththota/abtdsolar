package com.abtd.solarbackend.mail.service.impl;

import com.abtd.solarbackend.mail.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Override
    public void sendEmail(
            String to,
            String subject,
            String body) {

        SimpleMailMessage message =
                new SimpleMailMessage();

        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);

        mailSender.send(message);
    }

    @Override
    public void sendEmailWithAttachment(
            String to,
            String subject,
            String body,
            byte[] attachment,
            String fileName) {

        try {

            MimeMessage message =
                    mailSender.createMimeMessage();

            MimeMessageHelper helper =
                    new MimeMessageHelper(
                            message,
                            true);

            helper.setTo(to);

            helper.setSubject(subject);

            helper.setText(body);

            helper.addAttachment(
                    fileName,
                    new ByteArrayResource(attachment));

            mailSender.send(message);

        } catch (MessagingException ex) {

            throw new RuntimeException(
                    "Failed to send email.",
                    ex);
        }
    }
}