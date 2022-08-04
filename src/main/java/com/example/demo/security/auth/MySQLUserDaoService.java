package com.example.demo.security.auth;

import com.example.demo.security.registration.model.User;
import com.example.demo.security.registration.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

import static com.example.demo.security.registration.model.UserRole.ADMIN;
import static com.example.demo.security.registration.model.UserRole.USER;
@Repository("MySQL")
@RequiredArgsConstructor
public class MySQLUserDaoService implements ApplicationUserDao {

    private final UserRepository userRepository;

    @Override
    public Optional<ApplicationUser> selectApplicationUserByUsername(String email) {

        User user = userRepository.findByEmail(email);

        return Optional.of(new ApplicationUser(
                user.getEmail(),
                user.getPassword(),
                true,
                true,
                true,
                user.isEnabled(),
                getAuthorities(user.getRole())
        ));
    }

    private Set<SimpleGrantedAuthority> getAuthorities(String role) {
        if (role.equals("ADMIN")) {
            return ADMIN.getGrantedAuthorities();
        }

        return USER.getGrantedAuthorities();
    }
}
