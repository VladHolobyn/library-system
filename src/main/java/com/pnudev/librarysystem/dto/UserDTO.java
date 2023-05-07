package com.pnudev.librarysystem.dto;

import com.pnudev.librarysystem.enums.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UserDTO {
    @NotNull
    private Long id;

    @NotBlank(message = "First name must be provided")
    private String firstName;

    @NotBlank(message = "Last name must be provided")
    private String lastName;

    @NotBlank(message = "Email must be provided")
    @Email(message = "Wrong email format")
    private String email;

    @NotBlank(message = "Address must be provided")
    private String address;

    @NotBlank(message = "Phone number must be provided")
    @Pattern(regexp = "^\\d{10}$", message = "Wrong phone format")
    private String phoneNumber;

    @NotNull(message = "Role must be provided")
    private UserRole role;
}
