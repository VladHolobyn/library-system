package com.pnudev.librarysystem.dto.borrowing;

import com.pnudev.librarysystem.dto.book.BookDTO;
import com.pnudev.librarysystem.dto.user.UserDTO;
import com.pnudev.librarysystem.enums.BorrowingStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class BorrowingDTO {
    @NotNull(message = "Id must be provided")
    private Long id;

    @NotNull(message = "User must be provided")
    private UserDTO user;

    @NotNull(message = "Book must be provided")
    private BookDTO book;

    private LocalDate reservationDate;
    private LocalDate checkoutDate;
    private LocalDate dueDate;
    private LocalDate returnDate;

    @NotNull(message = "Reservation must be provided")
    private BorrowingStatus status;
}
