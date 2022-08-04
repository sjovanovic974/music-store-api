package com.example.demo.security.registration.service;

import com.example.demo.security.registration.dto.UserDTO;
import com.example.demo.security.registration.model.User;
import com.example.demo.security.registration.model.VerificationToken;

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

    boolean checkIfValidOldPassword(User user, String oldPassword);
}
