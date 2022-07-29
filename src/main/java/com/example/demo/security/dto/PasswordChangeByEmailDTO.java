package com.example.demo.security.dto;

import lombok.Data;

import javax.validation.constraints.*;

@Data
public class PasswordChangeByEmailDTO {

    @NotNull
    @NotBlank
    @Email(message = "Enter valid email!")
    private String email;

}
