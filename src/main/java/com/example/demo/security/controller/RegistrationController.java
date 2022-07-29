package com.example.demo.security.controller;

import com.example.demo.security.dto.PasswordChangeByEmailDTO;
import com.example.demo.security.dto.PasswordSaveDTO;
import com.example.demo.security.dto.UserDTO;
import com.example.demo.security.event.PasswordResetTokenEvent;
import com.example.demo.security.event.RegistrationCompleteEvent;
import com.example.demo.security.event.ResendTokenEvent;
import com.example.demo.security.model.User;
import com.example.demo.security.model.VerificationToken;
import com.example.demo.security.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/registration")
@RequiredArgsConstructor
public class RegistrationController {

    private final UserService userService;

    private final ApplicationEventPublisher publisher;

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public String registerUser(@Valid @RequestBody UserDTO userDTO, HttpServletRequest http) {
        User user = userService.registerUser(userDTO);
        publisher.publishEvent(new RegistrationCompleteEvent(
                user,
                applicationUrl(http)
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

    @GetMapping("/verifyRegistration")
    public String verifyRegistration(@RequestParam("token") String token) {
        boolean result = userService.validateVerificationToken(token);
        if (result) {
            return "User verified successfully";
        }

        return "Bad user!";
    }

    @GetMapping("/resendToken")
    public String resendVerificationToken(
            @RequestParam("token") String oldToken, HttpServletRequest http) {

        VerificationToken verificationToken =
                userService.generateNewVerificationToken(oldToken);

        User user = verificationToken.getUser();
        publisher.publishEvent(new ResendTokenEvent(
                user,
                applicationUrl(http),
                verificationToken.getToken()
        ));

        return "Verification link sent!";
    }

    @PostMapping("/resetPassword")
    public String resetPassword(
            @Valid @RequestBody PasswordChangeByEmailDTO passwordChangeByEmailDTO, HttpServletRequest http) {
        User user = userService.findUserByEmail(passwordChangeByEmailDTO.getEmail());
        String url = "";
        if (user != null) {
            String token = UUID.randomUUID().toString();
            userService.createPasswordResetTokenForUser(user, token);
            url = applicationUrl(http)
                    + "/savePassword?token="
                    + token;

            publisher.publishEvent(new PasswordResetTokenEvent(
                    user,
                    url
            ));
            return "Password reset link sent!";
        }
        return "Bad user!";
    }

    @PostMapping("/savePassword")
    public String savePassword(@RequestParam("token") String token, @Valid @RequestBody PasswordSaveDTO passwordDTO) {
        boolean result = userService.validatePasswordResetToken(token);
        if (!result) {
            return "Invalid token!";
        }

        Optional<User> user = userService.getUserByPasswordResetToken(token);
        if (user.isPresent()) {
            userService.changePassword(user.get(), passwordDTO.getNewPassword());
            userService.deletePasswordResetToken(token);
            return "Password reset successful.";
        }

        return "Invalid token!";
    }
}

