package com.pnudev.librarysystem.dto.borrowing;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class BorrowingCheckoutDTO {
    @NotNull(message = "Due date must be provided")
    private LocalDate dueDate;
}
