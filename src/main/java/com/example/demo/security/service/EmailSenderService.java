package com.example.demo.security.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailSenderService {

    private final JavaMailSender mailSender;

    public void sendEmail(String toEmail, String body, String subject) throws MessagingException {

        MimeMessage mailMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mailMessage);

        helper.setFrom("sjovanovic974@gmail.com");
        helper.setTo(toEmail);
        helper.setText(body, true);
        helper.setSubject(subject);

        mailSender.send(mailMessage);
        log.info("Email sent to {}", toEmail);
    }
}
