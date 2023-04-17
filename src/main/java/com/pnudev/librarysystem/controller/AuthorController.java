package com.pnudev.librarysystem.controller;

import com.pnudev.librarysystem.dto.AuthorDTO;
import com.pnudev.librarysystem.service.AuthorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
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

import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/authors")
public class AuthorController {

    @Value("${application.defaultPageNumber}")
    private int defaultPageNumber;
    @Value("${application.defaultPageSize}")
    private int defaultPageSize;

    private final AuthorService authorService;

    @GetMapping
    public Page<AuthorDTO> searchAuthor(
            @RequestParam("firstname") Optional<String> firstName,
            @RequestParam("lastname") Optional<String> lastName,
            @RequestParam("page") Optional<Integer> pageNumber,
            @RequestParam("size") Optional<Integer> pageSize
    ){
        return authorService.searchAuthorByParams(
                firstName,
                lastName,
                pageNumber.filter(p -> p > 0).orElse(defaultPageNumber),
                pageSize.filter(s -> s > 0).orElse(defaultPageSize)
        );
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AuthorDTO addAuthor(@Valid @RequestBody AuthorDTO authorDTO){
        return authorService.addAuthor(authorDTO);
    }

    @PutMapping("/{id}")
    public AuthorDTO updateAuthor(@PathVariable Long id, @Valid @RequestBody AuthorDTO authorDTO){
        return authorService.updateAuthor(id, authorDTO);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAuthor(@PathVariable Long id){
        authorService.deleteAuthor(id);
    }
}
