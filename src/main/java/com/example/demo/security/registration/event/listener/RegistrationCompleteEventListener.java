package com.example.demo.security.registration.event.listener;

import com.example.demo.security.registration.event.RegistrationCompleteEvent;
import com.example.demo.security.registration.model.User;
import com.example.demo.security.registration.service.EmailSenderService;
import com.example.demo.security.registration.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class RegistrationCompleteEventListener implements
        ApplicationListener<RegistrationCompleteEvent> {

    private final UserService userService;
    private final EmailSenderService emailSenderService;

    @Override
    public void onApplicationEvent(RegistrationCompleteEvent event) {
        // create the verification token for the user with a link
        User user = event.getUser();
        String token = UUID.randomUUID().toString();

        userService.saveVerificationTokenForUser(token, user);
        // send email to the user
        String url = event.getApplicationUrl() + "/api/verifyRegistration?token=" + token;
        String emailBody = "<p>Click on link <a href=" + url + ">" + url + "</a> to " +
                "confirm your registration</p>";

        try {
            emailSenderService.sendEmail(user.getEmail(), emailBody, "Registration Confirmation");
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
