package com.example.demo.security.dto;

import com.example.demo.security.validation.ValidEmail;
import lombok.Data;

import javax.validation.constraints.*;

@Data
public class PasswordChangeByEmailDTO {

    @NotNull
    @NotBlank
    @ValidEmail
    private String email;

}
