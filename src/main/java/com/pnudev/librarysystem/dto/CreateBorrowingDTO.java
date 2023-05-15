package com.pnudev.librarysystem.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateBorrowingDTO {
    @NotNull(message = "Book id must be provided")
    private Long bookId;
}
