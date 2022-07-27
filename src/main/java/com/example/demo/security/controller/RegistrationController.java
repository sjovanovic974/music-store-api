package com.example.demo.security.controller;

import com.example.demo.security.dto.UserDTO;
import com.example.demo.security.event.RegistrationCompleteEvent;
import com.example.demo.security.model.User;
import com.example.demo.security.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class RegistrationController {

    private final UserService userService;

    private ApplicationEventPublisher publisher;

    @PostMapping()
    public String registerUser(@RequestBody UserDTO userDTO, HttpServletRequest httpServletRequest) {
        User user = userService.registerUser(userDTO);
        publisher.publishEvent(new RegistrationCompleteEvent(
                user,
                applicationUrl(httpServletRequest)
        ));
        return "User successfully registered.";
    }

    private String applicationUrl(HttpServletRequest httpServletRequest) {
        return "http://" +
                httpServletRequest.getServerName() +
                ":" +
                httpServletRequest.getServerPort() +
                httpServletRequest.getContextPath();
    }
}
