package com.pnudev.librarysystem.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class BookCheckoutDTO {
    private LocalDate dueDate;
}
