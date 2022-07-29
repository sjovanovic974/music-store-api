package com.example.demo.security.event;

import com.example.demo.security.model.User;
import com.example.demo.security.model.VerificationToken;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;
@Getter
@Setter
public class ResendTokenEvent extends ApplicationEvent {

    private User user;
    private String applicationUrl;
    private String verificationToken;

    public ResendTokenEvent(User user, String applicationUrl, String verificationToken) {
        super(user);
        this.user = user;
        this.applicationUrl = applicationUrl;
        this.verificationToken = verificationToken;
    }
}