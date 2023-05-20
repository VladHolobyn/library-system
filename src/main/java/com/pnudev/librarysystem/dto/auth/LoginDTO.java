package com.pnudev.librarysystem.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginDTO {

    @NotBlank(message = "Email must be provided")
    @Email(message = "Invalid email")
    private String email;

    @NotBlank(message = "Password must be provided")
    private String password;
}
