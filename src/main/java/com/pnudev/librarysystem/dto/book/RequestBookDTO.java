package com.pnudev.librarysystem.dto.book;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class RequestBookDTO {
    @NotBlank(message = "Title must be specified")
    private String title;

    @NotNull(message = "Cover image name must be specified")
    private String coverImageName;

    @NotBlank(message = "Description must be specified")
    private String description;

    @NotNull(message = "Publication year must be specified")
    private Integer publicationYear;

    @NotNull(message = "Quantity must be specified")
    private Integer quantity;

    @NotNull(message = "Author IDs must be specified")
    private List<Long> authorIds;

    @NotNull(message = "Category IDs must be specified")
    private List<Long> categoryIds;
}
