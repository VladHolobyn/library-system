package com.pnudev.librarysystem.service;

import com.pnudev.librarysystem.dto.CreateBorrowingDTO;
import com.pnudev.librarysystem.entity.Borrowing;
import com.pnudev.librarysystem.enums.BorrowingStatus;
import com.pnudev.librarysystem.exception.OperationFailedException;
import com.pnudev.librarysystem.repository.BorrowingRepository;
import com.pnudev.librarysystem.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@RequiredArgsConstructor
@Service
public class BorrowingService {
    private final UserService userService;
    private final BookService bookService;
    private final BorrowingRepository borrowingRepository;


    public void reserveBook(UserDetailsImpl userDetailsImpl, CreateBorrowingDTO createBorrowingDTO) {
        Long userId = userDetailsImpl.getId();
        Long bookId = createBorrowingDTO.getBookId();

        if (!bookService.isBookAvailable(bookId)) {
            throw new OperationFailedException("Book is not available");
        }

        if (!userService.canUserReserve(userId)) {
            throw new OperationFailedException("User has reached the maximum number of reservations");
        }

        Borrowing borrowing = Borrowing.builder()
                .userId(userId)
                .bookId(bookId)
                .reservationDate(LocalDate.now())
                .status(BorrowingStatus.RESERVED)
                .build();
        borrowingRepository.save(borrowing);
    }
}
