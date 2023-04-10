package com.pnudev.librarysystem.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;


@Data
public class CategoryDTO {
    private Long id;
    @NotBlank(message = "Name must be specified")
    private String name;
}
