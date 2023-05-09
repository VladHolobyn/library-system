package com.pnudev.librarysystem.repository;

import com.pnudev.librarysystem.entity.Borrowing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

public interface BorrowingRepository extends JpaRepository<Borrowing, Long> {
    @Query(nativeQuery = true,
            value = "SELECT * FROM borrowing b WHERE cast(b.status as text) != 'RETURNED' AND b.user_id = ?1")
    List<Borrowing> findAllActiveUserBorrowings(Long userId);

    @Query(nativeQuery = true,
            value = "DELETE FROM Borrowing b WHERE cast(b.status as text) = 'RESERVED' and b.reservation_date < ?1")
    @Modifying
    @Transactional
    void deleteAllReservationByDateIsBefore(LocalDate time);

}
