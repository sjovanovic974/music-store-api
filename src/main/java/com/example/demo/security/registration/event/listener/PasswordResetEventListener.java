package com.example.demo.security.registration.event.listener;

import com.example.demo.security.registration.event.PasswordResetTokenEvent;
import com.example.demo.security.registration.model.User;
import com.example.demo.security.registration.service.EmailSenderService;
import com.example.demo.security.registration.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;

@Component
@RequiredArgsConstructor
public class PasswordResetEventListener implements
        ApplicationListener<PasswordResetTokenEvent> {

    private final UserService userService;
    private final EmailSenderService emailSenderService;

    @Override
    public void onApplicationEvent(PasswordResetTokenEvent event) {
        // create the verification token for the user with a link
        User user = event.getUser();
        String url = event.getApplicationUrl();

        // send email to the user
        String emailBody = "<p>Click on link <a href=" + url + ">" + url + "</a> to " +
                "change your password</p>";

        try {
            emailSenderService.sendEmail(user.getEmail(), emailBody, "Reset password");
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
