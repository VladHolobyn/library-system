package com.pnudev.librarysystem.controller;

import com.pnudev.librarysystem.dto.book.BookDTO;
import com.pnudev.librarysystem.dto.book.RequestBookDTO;
import com.pnudev.librarysystem.security.UserDetailsImpl;
import com.pnudev.librarysystem.service.BookAvailabilitySubscriptionService;
import com.pnudev.librarysystem.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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


@RequiredArgsConstructor
@RestController
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;
    private final BookAvailabilitySubscriptionService bookSubscriptionService;

    @PreAuthorize("hasAnyAuthority('ADMIN', 'CLIENT')")
    @GetMapping
    public Page<BookDTO> searchBook(
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "authorLastName", required = false) String authorLastName,
            @RequestParam(value = "category", required = false) String category,
            @PageableDefault Pageable pageable
    ) {
        return bookService.searchBook(title, authorLastName, category, pageable);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'CLIENT')")
    @GetMapping("/{id}")
    public BookDTO getBookById(@PathVariable Long id) {
        return bookService.getBookById(id);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'CLIENT')")
    @GetMapping(value = "/{id}/image", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public byte[] getBookImage(@PathVariable Long id) {
        return bookService.getBookImage(id);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createBook(@Valid @RequestBody RequestBookDTO requestBookDTO) {
        bookService.createBook(requestBookDTO);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/images")
    @ResponseStatus(HttpStatus.CREATED)
    public String uploadBookImage(@RequestParam MultipartFile file) {
        return bookService.uploadCoverImage(file);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/{id}")
    public void updateBook(@PathVariable Long id, @Valid @RequestBody RequestBookDTO requestBookDTO) {
        bookService.updateBook(id, requestBookDTO);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
    }

    @PreAuthorize("hasAuthority('CLIENT')")
    @PostMapping("/{id}/subscribe")
    @ResponseStatus(HttpStatus.CREATED)
    public void subscribeForBookAvailability(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl user) {
        bookService.subscribeForBookAvailability(id, user);
    }

    @PreAuthorize("hasAuthority('CLIENT')")
    @DeleteMapping("/{id}/unsubscribe")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void unsubscribeFromBookAvailability(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl user) {
        bookSubscriptionService.unsubscribe(id, user.getId());
    }
}
