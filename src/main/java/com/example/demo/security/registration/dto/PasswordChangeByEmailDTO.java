package com.example.demo.security.registration.dto;

import com.example.demo.security.registration.validation.ValidEmail;
import lombok.Data;

import javax.validation.constraints.*;

@Data
public class PasswordChangeByEmailDTO {

    @NotNull
    @NotBlank
    @ValidEmail
    private String email;

}
