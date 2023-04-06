package com.pnudev.librarysystem.repository;

import com.pnudev.librarysystem.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}