package com.example.demo.security.registration.controller;

import com.example.demo.security.registration.dto.ChangePasswordDTO;
import com.example.demo.security.registration.dto.PasswordChangeByEmailDTO;
import com.example.demo.security.registration.dto.PasswordSaveDTO;
import com.example.demo.security.registration.dto.UserDTO;
import com.example.demo.security.registration.event.PasswordResetTokenEvent;
import com.example.demo.security.registration.event.RegistrationCompleteEvent;
import com.example.demo.security.registration.event.ResendTokenEvent;
import com.example.demo.security.registration.model.User;
import com.example.demo.security.registration.model.VerificationToken;
import com.example.demo.security.registration.service.UserService;
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

        if (user != null) {
            String token = UUID.randomUUID().toString();
            userService.createPasswordResetTokenForUser(user, token);
            String url = applicationUrl(http)
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
    public String savePassword(@RequestParam("token") String token,
                               @Valid @RequestBody PasswordSaveDTO passwordDTO) {
        boolean result = userService.validatePasswordResetToken(token);
        if (!result) {
            return "Invalid token!";
        }

        Optional<User> user = userService.getUserByPasswordResetToken(token);
        if (user.isPresent()) {
            userService.changePassword(user.get(), passwordDTO.getNewPassword());
            userService.deletePasswordResetToken(token);
            return "Password changed successfully.";
        }

        return "Invalid token!";
    }
    @PostMapping("/changePassword")
    public String changePassword(@Valid @RequestBody ChangePasswordDTO changePasswordDTO) {
        User user = userService.findUserByEmail(changePasswordDTO.getEmail());

        if (user == null) {
            return "Invalid user!";
        }

        if (!userService.checkIfValidOldPassword(user, changePasswordDTO.getOldPassword())) {
            return "Invalid old password!";
        }

        //Save new password
        userService.changePassword(user, changePasswordDTO.getNewPassword());

        return "Password changed successfully";
    }
}

