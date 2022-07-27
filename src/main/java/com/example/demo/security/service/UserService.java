package com.example.demo.security.service;

import com.example.demo.security.dto.UserDTO;
import com.example.demo.security.model.User;

public interface UserService {
    User registerUser(UserDTO userDTO);

    void saveVerificationTokenForUser(String token, User user);
}
