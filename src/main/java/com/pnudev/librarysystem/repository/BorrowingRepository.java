package com.pnudev.librarysystem.repository;

import com.pnudev.librarysystem.entity.Borrowing;
import com.pnudev.librarysystem.enums.BorrowingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

public interface BorrowingRepository extends JpaRepository<Borrowing, Long>, JpaSpecificationExecutor<Borrowing> {

    List<Borrowing> findAllByStatusIsNotAndUserId(BorrowingStatus status, Long id);

    @Transactional
    void deleteAllByStatusAndReservationDateIsBefore(BorrowingStatus status, LocalDate time);

}
