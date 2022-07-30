package com.example.demo.security.dto;

import com.example.demo.security.validation.ValidEmail;
import lombok.Data;

import javax.validation.constraints.*;
@Data
public class ChangePasswordDTO {

    @NotNull
    @NotBlank
    @ValidEmail
    private String email;

    @NotNull
    @NotBlank(message = "Old password field cannot be empty!")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,20}$",
            message = "Enter valid password!")
    private String oldPassword;

    @NotNull
    @NotBlank(message = "New password field cannot be empty!")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,20}$",
            message = "Enter valid password!")
    private String newPassword;

    private String matchingPassword;

    @AssertTrue(message = "Old and new passwords should match")
    public boolean isPasswordsEqual() {
        try {
            return newPassword.equals(matchingPassword);
        } catch (NullPointerException e) {
            return false;
        }
    }
}
