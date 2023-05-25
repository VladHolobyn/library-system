package com.pnudev.librarysystem.dto.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthenticationResponseDTO {

    @NotBlank(message = "JWT must be provided")
    private String jwt;
}
