package com.pnudev.librarysystem.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AuthorDTO {
    private Long id;

    @NotBlank(message = "Firstname must be specified")
    private String firstName;

    @NotBlank(message = "Lastname must be specified")
    private String lastName;
}
