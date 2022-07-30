package com.example.demo.security.service;

import com.example.demo.error.exceptions.CustomBadRequestException;
import com.example.demo.security.dto.UserDTO;
import com.example.demo.security.model.PasswordResetToken;
import com.example.demo.security.model.User;
import com.example.demo.security.model.VerificationToken;
import com.example.demo.security.repository.PasswordResetTokenRepository;
import com.example.demo.security.repository.UserRepository;
import com.example.demo.security.repository.VerificationTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Calendar;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final PasswordResetTokenRepository passwordResetTokenRepository;

    @Override
    public User registerUser(UserDTO userDTO) {
        User user = new User();
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setEmail(userDTO.getEmail());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setRole("USER");

        return userRepository.save(user);
    }

    @Override
    public void saveVerificationTokenForUser(String token, User user) {
        VerificationToken verificationToken =
                new VerificationToken(token, user);

        verificationTokenRepository.save(verificationToken);
    }

    @Override
    public boolean validateVerificationToken(String token) {
        VerificationToken verificationToken =
                verificationTokenRepository.findByToken(token);

        if (verificationToken == null) {
            throw new CustomBadRequestException("Token not valid!");
        }

        Calendar calendar = Calendar.getInstance();

        if (verificationToken.getExpirationTime().getTime() -
                calendar.getTime().getTime() <= 0) {
            throw new CustomBadRequestException("Token expired!");
        }

        User user = verificationToken.getUser();

        user.setEnabled(true);
        userRepository.save(user);

        verificationTokenRepository.delete(verificationToken);

        return true;
    }

    @Override
    public VerificationToken generateNewVerificationToken(String oldToken) {
        VerificationToken verificationToken =
                verificationTokenRepository.findByToken(oldToken);
        if (verificationToken == null) {
            throw new CustomBadRequestException("Invalid token!");
        }

        verificationToken.setToken(UUID.randomUUID().toString());
        verificationToken.setExpirationTime(VerificationToken.calculateExpirationDate());
        verificationTokenRepository.save(verificationToken);

        return verificationToken;
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public void createPasswordResetTokenForUser(User user, String token) {
        PasswordResetToken passwordResetToken = new PasswordResetToken(token, user);
        passwordResetTokenRepository.save(passwordResetToken);
    }

    @Override
    public boolean validatePasswordResetToken(String token) {
        PasswordResetToken passwordResetToken =
                passwordResetTokenRepository.findByToken(token);

        if (passwordResetToken == null) {
            throw new CustomBadRequestException("Password reset token not valid!");
        }

        Calendar calendar = Calendar.getInstance();

        if (passwordResetToken.getExpirationTime().getTime() -
                calendar.getTime().getTime() <= 0) {
            throw new CustomBadRequestException("Token expired!");
        }

        return true;
    }

    @Override
    public Optional<User> getUserByPasswordResetToken(String token) {
        return Optional.ofNullable(passwordResetTokenRepository.findByToken(token).getUser());
    }

    @Override
    public void changePassword(User user, String newPassword) {
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    @Override
    public void deletePasswordResetToken(String token) {
        passwordResetTokenRepository.deleteByToken(token);
    }

    @Override
    public boolean checkIfValidOldPassword(User user, String oldPassword) {
        return passwordEncoder.matches(oldPassword, user.getPassword());
    }
}

