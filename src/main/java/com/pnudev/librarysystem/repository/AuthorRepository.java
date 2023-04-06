package com.pnudev.librarysystem.repository;

import com.pnudev.librarysystem.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorRepository extends JpaRepository<Author, Long> {
}