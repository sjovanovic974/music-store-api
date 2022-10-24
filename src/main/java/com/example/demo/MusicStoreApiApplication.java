package com.example.demo;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Music Shop API",
        version = "1.0.0", description = "An API to run a music shop"),
        servers = {@Server(url = "http://localhost:8080")},
        tags = {
            @Tag(name = "api", description = "main resource 'product(s)'"),
            @Tag(name = "user", description = "Available to registered and authenticated users"),
            @Tag(name = "admin", description = "Available only to administrator"),
            @Tag(name = "registration", description = "Publicly available endpoints for user registration")
})
@SecurityScheme(type = SecuritySchemeType.HTTP, name = "BearerJWT", scheme = "bearer", bearerFormat = "JWT")
public class MusicStoreApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(MusicStoreApiApplication.class, args);
    }

}