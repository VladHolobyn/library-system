package com.pnudev.librarysystem.controller;

import com.pnudev.librarysystem.dto.CreateBorrowingDTO;
import com.pnudev.librarysystem.security.UserDetailsImpl;
import com.pnudev.librarysystem.service.BorrowingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/borrowings")
public class BorrowingController {
    private final BorrowingService borrowingService;

    @PreAuthorize("hasAuthority('CLIENT')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void reserveBook(
            @AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
            @Valid @RequestBody CreateBorrowingDTO createBorrowingDTO) {
        borrowingService.reserveBook(userDetailsImpl, createBorrowingDTO);
    }
}
