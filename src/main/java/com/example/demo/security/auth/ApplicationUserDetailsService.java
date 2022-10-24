package com.example.demo.security.auth;

import com.example.demo.security.registration.model.User;
import com.example.demo.security.registration.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ApplicationUserDetailsService implements UserDetailsService {

    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        User user = userService.findUserByEmail(email);

        if (user == null) {
            throw new UsernameNotFoundException("Username " + email + " not found!");
        }

        return new ApplicationUser(user);
    }
}