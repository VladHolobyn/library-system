package com.pnudev.librarysystem.controller;

import com.pnudev.librarysystem.dto.AuthorDTO;
import com.pnudev.librarysystem.service.AuthorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
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
@RequestMapping("/authors")
public class AuthorController {

    private final AuthorService authorService;

    @Secured({"ADMIN", "CLIENT"})
    @GetMapping
    public Page<AuthorDTO> searchAuthor(
            @RequestParam(value = "firstname", required = false) String firstName,
            @RequestParam(value = "lastname", required = false) String lastName,
            @PageableDefault Pageable pageable
    ) {
        return authorService.searchAuthorByParams(firstName, lastName, pageable);
    }

    @Secured("ADMIN")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AuthorDTO addAuthor(@Valid @RequestBody AuthorDTO authorDTO) {
        return authorService.addAuthor(authorDTO);
    }

    @Secured("ADMIN")
    @PutMapping("/{id}")
    public AuthorDTO updateAuthor(@PathVariable Long id, @Valid @RequestBody AuthorDTO authorDTO) {
        return authorService.updateAuthor(id, authorDTO);
    }

    @Secured("ADMIN")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAuthor(@PathVariable Long id) {
        authorService.deleteAuthor(id);
    }
}
