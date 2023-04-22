package com.pnudev.librarysystem.controller;

import com.pnudev.librarysystem.dto.BookDTO;
import com.pnudev.librarysystem.dto.FormBookDTO;
import com.pnudev.librarysystem.service.BookService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("books")
public class BookController {

    private final BookService bookService;

    @PostMapping
    public BookDTO createBook(@Valid @RequestBody FormBookDTO formBookDTO) {
        return bookService.addBook(formBookDTO);
    }
}
