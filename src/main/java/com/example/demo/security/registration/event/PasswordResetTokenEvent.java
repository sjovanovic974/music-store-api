package com.example.demo.security.registration.event;

import com.example.demo.security.registration.model.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;
@Getter
@Setter
public class PasswordResetTokenEvent extends ApplicationEvent {

    private User user;
    private String applicationUrl;

    public PasswordResetTokenEvent(User user, String applicationUrl) {
        super(user);
        this.user = user;
        this.applicationUrl = applicationUrl;
    }
}