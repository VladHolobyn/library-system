package com.pnudev.librarysystem.controller;

import com.pnudev.librarysystem.dto.CategoryDTO;
import com.pnudev.librarysystem.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;


@RequiredArgsConstructor
@RestController
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;

    @PreAuthorize("hasAnyAuthority('ADMIN', 'CLIENT')")
    @GetMapping
    public Page<CategoryDTO> searchCategory(
            @RequestParam("name") String name,
            @PageableDefault Pageable pageable
    ) {
        return categoryService.searchCategory(name, pageable);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createCategory(@Valid @RequestBody CategoryDTO categoryDTO) {
        categoryService.createCategory(categoryDTO);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/{id}")
    public void updateCategory(@PathVariable Long id, @Valid @RequestBody CategoryDTO categoryDTO) {
        categoryService.updateCategory(id, categoryDTO);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
    }

}
