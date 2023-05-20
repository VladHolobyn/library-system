package com.pnudev.librarysystem.dto.book;

import com.pnudev.librarysystem.dto.author.AuthorDTO;
import com.pnudev.librarysystem.dto.category.CategoryDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class BookDTO {
    private Long id;

    @NotBlank(message = "Title must be specified")
    private String title;

    @NotBlank(message = "Cover image url must be specified")
    private String coverImageUrl;

    @NotBlank(message = "Description must be specified")
    private String description;

    @NotNull(message = "Publication year must be specified")
    private Integer publicationYear;

    @NotNull(message = "Quantity must be specified")
    private Integer quantity;

    @NotNull(message = "Authors must be specified")
    private List<AuthorDTO> authors;

    @NotNull(message = "Categories must be specified")
    private List<CategoryDTO> categories;
}
