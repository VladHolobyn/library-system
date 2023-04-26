package com.pnudev.librarysystem.controller;

import com.pnudev.librarysystem.dto.BookDTO;
import com.pnudev.librarysystem.dto.RequestBookDTO;
import com.pnudev.librarysystem.service.BookService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
import org.springframework.web.multipart.MultipartFile;


@AllArgsConstructor
@RestController
@RequestMapping("books")
public class BookController {

    private final BookService bookService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Long createBook(@Valid @RequestBody RequestBookDTO requestBookDTO) {
        return bookService.createBook(requestBookDTO);
    }

    @PostMapping("/images")
    @ResponseStatus(HttpStatus.CREATED)
    public String uploadBookImage(@RequestParam MultipartFile file) {
        return bookService.uploadCoverImage(file);
    }

    @GetMapping(value = "/{id}/image", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public byte[] getBookImage(@PathVariable Long id) {
        return bookService.getBookImage(id);
    }

    @GetMapping
    public Page<BookDTO> searchBook(
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "authorLastName", required = false) String authorLastname,
            @RequestParam(value = "category", required = false) String categoryName,
            @PageableDefault Pageable pageable
    ) {
        return bookService.searchBook(title, authorLastname, categoryName, pageable);
    }

    @GetMapping("/{id}")
    public BookDTO getBookById(@PathVariable Long id) {
        return bookService.getBookById(id);
    }

    @PutMapping("/{id}")
    public BookDTO updateBook(@PathVariable Long id, @Valid @RequestBody RequestBookDTO requestBookDTO) {
        return bookService.updateBook(id, requestBookDTO);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBook(@PathVariable Long id) {
        bookService.deleteBookById(id);
    }

}
