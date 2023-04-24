package com.pnudev.librarysystem.controller;

import com.pnudev.librarysystem.dto.BookDTO;
import com.pnudev.librarysystem.dto.FormBookDTO;
import com.pnudev.librarysystem.service.BookService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;



@AllArgsConstructor
@RestController
@RequestMapping("books")
public class BookController {

    private final BookService bookService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookDTO createBook(@Valid @ModelAttribute FormBookDTO formBookDTO) {
        return bookService.createBook(formBookDTO);
    }

//    @GetMapping(value = "/{id}/image",  produces = MediaType.IMAGE_JPEG_VALUE)
//    public byte[] getBookImage(@PathVariable Long id){
//        return bookService.getBookImage(id);
//    }

    @PutMapping("/{id}")
    public BookDTO updateBook(@PathVariable Long id, @Valid @ModelAttribute FormBookDTO formBookDTO) {
        return bookService.updateBook(id, formBookDTO);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBook(@PathVariable Long id){
        bookService.deleteBookById(id);
    }
}
