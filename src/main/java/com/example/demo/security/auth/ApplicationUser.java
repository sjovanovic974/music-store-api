package com.example.demo.security.auth;

import com.example.demo.security.registration.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

import static com.example.demo.security.registration.model.UserRole.ADMIN;
import static com.example.demo.security.registration.model.UserRole.USER;
@RequiredArgsConstructor
public class ApplicationUser implements UserDetails {

    private final User user;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        String role = this.user.getRole();
        if (role.equals("ADMIN")) {
            return ADMIN.getGrantedAuthorities();
        }

        return USER.getGrantedAuthorities();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return user.isEnabled();
    }
}
