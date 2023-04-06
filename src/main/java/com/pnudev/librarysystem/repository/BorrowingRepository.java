package com.pnudev.librarysystem.repository;

import com.pnudev.librarysystem.entity.Borrowing;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BorrowingRepository extends JpaRepository<Borrowing, Long> {
}