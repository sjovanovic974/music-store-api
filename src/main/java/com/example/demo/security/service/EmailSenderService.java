package com.example.demo.security.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailSenderService {

    private JavaMailSender mailSender;

    public void sendEmail(String toEmail, String body, String subject) {

        SimpleMailMessage mailMessage = new SimpleMailMessage();

        mailMessage.setFrom("sjovanovic974@gmail.com");
        mailMessage.setTo(toEmail);
        mailMessage.setText(body);
        mailMessage.setSubject(subject);

        mailSender.send(mailMessage);
        log.info("Email sent to {}", toEmail);
    }
}
