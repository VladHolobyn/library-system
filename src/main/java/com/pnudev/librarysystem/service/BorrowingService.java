package com.pnudev.librarysystem.service;

import com.pnudev.librarysystem.dto.borrowing.BorrowingCheckoutDTO;
import com.pnudev.librarysystem.dto.borrowing.BorrowingDTO;
import com.pnudev.librarysystem.dto.borrowing.CreateBorrowingDTO;
import com.pnudev.librarysystem.entity.Borrowing;
import com.pnudev.librarysystem.enums.BorrowingStatus;
import com.pnudev.librarysystem.exception.OperationFailedException;
import com.pnudev.librarysystem.mapper.BorrowingMapper;
import com.pnudev.librarysystem.repository.BorrowingRepository;
import com.pnudev.librarysystem.security.UserDetailsImpl;
import com.pnudev.librarysystem.specification.BorrowingSpecificationBuilder;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Service
public class BorrowingService {
    private static final String BORROWING_NOT_FOUND_MSG_FORMAT = "Borrowing with id: %d not found";

    private final UserService userService;
    private final BookService bookService;
    private final BorrowingRepository borrowingRepository;
    private final BorrowingMapper borrowingMapper;
    private final BorrowingSpecificationBuilder borrowingSpecificationBuilder;
    private final BookAvailabilitySubscriptionService bookAvailabilitySubscriptionService;

    @Value("${application.reservation.expiration-in-days}")
    private int reservationExpirationInDays;


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

    public void cancelReservation(UserDetailsImpl userDetailsImpl, Long reservationId) {
        Borrowing borrowing = borrowingRepository.findById(reservationId)
                .orElseThrow(() -> new EntityNotFoundException(BORROWING_NOT_FOUND_MSG_FORMAT.formatted(reservationId)));

        if (!borrowing.getUserId().equals(userDetailsImpl.getId())) {
            throw new OperationFailedException("User does not have borrowing with id: %d".formatted(reservationId));
        }

        if (borrowing.getStatus() != BorrowingStatus.RESERVED) {
            throw new OperationFailedException("Borrowing must have RESERVED status");
        }

        borrowingRepository.delete(borrowing);
        bookAvailabilitySubscriptionService.notify(borrowing.getBookId());
    }

    public List<BorrowingDTO> findActiveUserBorrowings(Long id) {
        List<Borrowing> borrowings = borrowingRepository.findAllByStatusIsNotAndUserId(BorrowingStatus.RETURNED, id);
        return borrowings.stream().map(borrowingMapper::toDTO).toList();
    }

    public void cleanUpExpiredReservation() {
        List<Borrowing> borrowings = borrowingRepository.deleteAllByStatusAndReservationDateIsBefore(BorrowingStatus.RESERVED, LocalDate.now().minusDays(reservationExpirationInDays));

        borrowings.stream()
                .distinct()
                .forEach(borrowing -> bookAvailabilitySubscriptionService.notify(borrowing.getBookId()));
    }

    public void checkoutBook(Long borrowingId, BorrowingCheckoutDTO borrowingCheckoutDTO) {
        Borrowing borrowing = borrowingRepository.findById(borrowingId)
                .orElseThrow(() -> new EntityNotFoundException(BORROWING_NOT_FOUND_MSG_FORMAT.formatted(borrowingId)));

        if (!borrowing.getStatus().equals(BorrowingStatus.RESERVED)) {
            throw new OperationFailedException("Borrowing must have RESERVED status");
        }
        if (borrowingCheckoutDTO.getDueDate().isBefore(LocalDate.now().plusDays(1))) {
            throw new OperationFailedException("Due date must be at least one day after checkout day");
        }

        borrowing.setStatus(BorrowingStatus.BORROWED);
        borrowing.setCheckoutDate(LocalDate.now());
        borrowing.setDueDate(borrowingCheckoutDTO.getDueDate());

        borrowingRepository.save(borrowing);
    }

    public void renewBook(Long borrowingId) {
        Borrowing borrowing = borrowingRepository.findById(borrowingId)
                .orElseThrow(() -> new EntityNotFoundException(BORROWING_NOT_FOUND_MSG_FORMAT.formatted(borrowingId)));

        if (!borrowing.getStatus().equals(BorrowingStatus.BORROWED)) {
            throw new OperationFailedException("Borrowing must have BORROWED status");
        }

        borrowing.setStatus(BorrowingStatus.RETURNED);
        borrowing.setReturnDate(LocalDate.now());
        borrowingRepository.save(borrowing);

        bookAvailabilitySubscriptionService.notify(borrowing.getBookId());
    }

    public Page<BorrowingDTO> searchBorrowing(String id, String status, String title, String lastName, Pageable pageable) {
        Specification<Borrowing> specification = Specification.where(null);

        if (StringUtils.isNotEmpty(id)) {
            specification = specification
                    .and(borrowingSpecificationBuilder.fieldContainsIgnoreCase("id", id));
        }
        if (StringUtils.isNotEmpty(status)) {
            specification = specification
                    .and(borrowingSpecificationBuilder.fieldContainsIgnoreCase("status", status));
        }
        if (StringUtils.isNotEmpty(title)) {
            specification = specification
                    .and(borrowingSpecificationBuilder.joinTableFieldContainsIgnoreCase("book", "title", title));
        }
        if (StringUtils.isNotEmpty(lastName)) {
            specification = specification
                    .and(borrowingSpecificationBuilder.joinTableFieldContainsIgnoreCase("user", "lastName", lastName));
        }


        Page<Borrowing> page = borrowingRepository.findAll(specification, pageable);
        return page.map(borrowingMapper::toDTO);
    }
}
