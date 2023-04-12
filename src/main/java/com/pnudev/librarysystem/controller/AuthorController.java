package com.pnudev.librarysystem.controller;

import com.pnudev.librarysystem.dto.AuthorDTO;
import com.pnudev.librarysystem.service.AuthorService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/authors")
public class AuthorController {
    private final AuthorService authorService;

    @PostMapping
    public AuthorDTO addAuthor(@Valid @RequestBody AuthorDTO authorDTO){
        return authorService.addAuthor(authorDTO);
    }
}
