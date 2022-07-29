package com.example.demo.security.event.listener;

import com.example.demo.security.event.ResendTokenEvent;
import com.example.demo.security.model.User;
import com.example.demo.security.service.EmailSenderService;
import com.example.demo.security.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;

@Component
@RequiredArgsConstructor
public class ResendTokenEventListener implements ApplicationListener<ResendTokenEvent> {

    private final UserService userService;
    private final EmailSenderService emailSenderService;

    @Override
    public void onApplicationEvent(ResendTokenEvent event) {
        User user = event.getUser();

        // send email to the user
        String url = event.getApplicationUrl() + "/api/verifyRegistration?token="
                + event.getVerificationToken();
        String emailBody = "<p>Click on the provided <a href=" + url + ">" + url + "</a> to " +
                "confirm your registration</p>";

        try {
            emailSenderService.sendEmail(user.getEmail(), emailBody, "Resent token");
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }


}
