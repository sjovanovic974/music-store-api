package com.example.demo.security.config;

import com.example.demo.security.auth.ApplicationUserService;
import com.example.demo.security.auth.jwt.JwtConfig;
import com.example.demo.security.auth.jwt.JwtTokenVerifier;
import com.example.demo.security.auth.jwt.JwtUsernameAndPasswordAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import javax.crypto.SecretKey;

import static com.example.demo.security.registration.model.UserPermission.PRODUCT_WRITE;
import static com.example.demo.security.registration.model.UserRole.ADMIN;
import static com.example.demo.security.registration.model.UserRole.USER;

@EnableWebSecurity
public class WebSecurityConfig {

    private static final String[] WHITE_LIST_URLS = {
            "/api/registration",
            "/api/registration/**"
    };

    private final PasswordEncoder passwordEncoder;
    private final ApplicationUserService applicationUserService;
    private final SecretKey secretKey;
    private final JwtConfig jwtConfig;

    public WebSecurityConfig(PasswordEncoder passwordEncoder,
                             ApplicationUserService applicationUserService,
                             SecretKey secretKey,
                             JwtConfig jwtConfig) {
        this.passwordEncoder = passwordEncoder;
        this.applicationUserService = applicationUserService;
        this.secretKey = secretKey;
        this.jwtConfig = jwtConfig;
    }

    private DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder);
        provider.setUserDetailsService(applicationUserService);
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilter(new JwtUsernameAndPasswordAuthenticationFilter(
                        authenticationManager(httpSecurity.getSharedObject(AuthenticationConfiguration.class)),
                        jwtConfig, secretKey))
                .addFilterAfter(new JwtTokenVerifier(secretKey, jwtConfig),
                        JwtUsernameAndPasswordAuthenticationFilter.class)
                .authorizeHttpRequests()
                .antMatchers(WHITE_LIST_URLS).permitAll()
                .antMatchers(HttpMethod.DELETE, "/api/products/**")
                    .hasAuthority(PRODUCT_WRITE.getPermission())
                .antMatchers(HttpMethod.PUT, "/api/products/**")
                    .hasAuthority(PRODUCT_WRITE.getPermission())
                .antMatchers(HttpMethod.POST, "/api/products/**")
                    .hasAuthority(PRODUCT_WRITE.getPermission())
                .antMatchers("/api/artists/**").hasRole(ADMIN.name())
                .antMatchers("/api/product-categories/**").hasRole(ADMIN.name())
                .antMatchers("/api/products/**").hasAnyRole(USER.name(), ADMIN.name())
                .anyRequest()
                .authenticated()
                .and()
                .authenticationProvider(daoAuthenticationProvider());

        return httpSecurity.build();
    }
}
