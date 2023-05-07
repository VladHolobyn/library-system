package com.pnudev.librarysystem.dto;

import com.pnudev.librarysystem.enums.UserRole;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UpdateUserDTO {
    @NotBlank(message = "First name must be provided")
    private String firstName;

    @NotBlank(message = "Last name must be provided")
    private String lastName;

    @NotBlank(message = "Address must be provided")
    private String address;

    @NotBlank(message = "Phone number must be provided")
    @Pattern(regexp = "^\\d{10}$", message = "Wrong phone format")
    private String phoneNumber;

    private String password;

    @NotNull(message = "Role must be provided")
    private UserRole role;
}
