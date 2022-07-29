package com.example.demo.security.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    @NotNull
    @Size(min = 2, max = 25)
    @NotBlank
    private String firstName;

    @NotNull
    @Size(min = 2, max = 25)
    @NotBlank
    private String lastName;

    @NotNull
    @NotBlank
    @Email(message = "Enter valid email!")
    private String email;

    @NotNull
    @NotBlank(message = "Password field cannot be empty!")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,20}$",
            message = "Enter valid password!")
    private String password;

    private String matchingPassword;

    @AssertTrue(message = "Passwords should match")
    public boolean isPasswordsEqual() {
        try {
            return password.equals(matchingPassword);
        } catch (NullPointerException e) {
            return false;
        }
    }
}
