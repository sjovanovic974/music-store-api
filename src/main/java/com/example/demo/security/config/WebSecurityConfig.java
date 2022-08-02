package com.example.demo.security.config;

import com.example.demo.security.model.UserRole;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static com.example.demo.security.model.UserPermission.PRODUCT_WRITE;
import static com.example.demo.security.model.UserRole.*;

@EnableWebSecurity
public class WebSecurityConfig {

    private static final String[] WHITE_LIST_URLS = {
            "/api/registration",
            "/api/registration/**"
    };

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(11);
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .cors()
                .and()
                .csrf()
                .disable()
                .authorizeHttpRequests()
                .antMatchers(WHITE_LIST_URLS).permitAll()
                .antMatchers(HttpMethod.DELETE, "/api/products/**").hasAuthority(PRODUCT_WRITE.getPermission())
                .antMatchers(HttpMethod.PUT, "/api/products/**").hasAuthority(PRODUCT_WRITE.getPermission())
                .antMatchers(HttpMethod.POST, "/api/products/**").hasAuthority(PRODUCT_WRITE.getPermission())
                .antMatchers("/api/artists/**").hasRole(ADMIN.name())
                .antMatchers("/api/product-categories/**").hasRole(ADMIN.name())
                .antMatchers("/api/products/**").hasAnyRole(USER.name(), ADMIN.name())
                .anyRequest()
                .authenticated()
                .and()
                .httpBasic();

        return httpSecurity.build();
    }
}
