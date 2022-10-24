package com.example.demo.security.config;

import com.example.demo.security.auth.ApplicationUserAuthenticationProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

import static com.example.demo.security.registration.model.UserPermission.PRODUCT_WRITE;
import static com.example.demo.security.registration.model.UserRole.ADMIN;
import static com.example.demo.security.registration.model.UserRole.USER;
import static org.springframework.http.HttpMethod.*;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    private static final String[] WHITE_LIST_URLS = {
            "/api/registration/**",
            "/swagger-ui/**",
            "/v3/api-docs/**"
    };

    private final ApplicationUserAuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        /*AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);

        authenticationManagerBuilder.authenticationProvider(authenticationProvider);*/

        return http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .antMatchers(WHITE_LIST_URLS).permitAll()
                        .antMatchers(DELETE, "/api/products/**").hasAuthority(PRODUCT_WRITE.getPermission())
                        .antMatchers(PUT, "/api/products/**").hasAuthority(PRODUCT_WRITE.getPermission())
                        .antMatchers(POST, "/api/products/**").hasAuthority(PRODUCT_WRITE.getPermission())
                        .antMatchers("/api/artists/**").hasRole(ADMIN.name())
                        .antMatchers("/api/product-categories/**").hasRole(ADMIN.name())
                        .antMatchers("/api/products/**").hasAnyRole(USER.name(), ADMIN.name())
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .httpBasic(Customizer.withDefaults())
                .authenticationProvider(authenticationProvider)
                .build();
    }
}
