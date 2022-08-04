package com.example.demo.security.registration.dto;

import lombok.Data;

import javax.validation.constraints.*;

@Data
public class PasswordSaveDTO {

    @NotNull
    @NotBlank(message = "Password field cannot be empty!")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,20}$",
            message = "Enter valid password!")
    private String newPassword;

}
