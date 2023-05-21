package com.pnudev.librarysystem.repository;

import com.pnudev.librarysystem.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CategoryRepository extends JpaRepository<Category, Long> {
    Page<Category> findAllByNameContainsIgnoreCase(String name, Pageable pageable);

    boolean existsByNameIgnoreCase(String name);
}
