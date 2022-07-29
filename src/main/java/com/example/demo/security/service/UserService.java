package com.example.demo.security.service;

import com.example.demo.security.dto.UserDTO;
import com.example.demo.security.model.User;
import com.example.demo.security.model.VerificationToken;

import java.util.Optional;

public interface UserService {
    User registerUser(UserDTO userDTO);

    void saveVerificationTokenForUser(String token, User user);

    boolean validateVerificationToken(String token);

    VerificationToken generateNewVerificationToken(String oldToken);

    User findUserByEmail(String email);

    void createPasswordResetTokenForUser(User user, String token);

    boolean validatePasswordResetToken(String token);

    Optional<User> getUserByPasswordResetToken(String token);

    void changePassword(User user, String newPassword);

    void deletePasswordResetToken(String token);
}
