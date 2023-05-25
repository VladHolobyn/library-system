package com.pnudev.librarysystem.dto.author;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AuthorDTO {
    private Long id;

    @NotBlank(message = "First name must be specified")
    private String firstName;

    @NotBlank(message = "Last name must be specified")
    private String lastName;
}
