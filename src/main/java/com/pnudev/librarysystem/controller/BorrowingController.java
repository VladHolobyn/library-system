package com.pnudev.librarysystem.controller;

import com.pnudev.librarysystem.dto.BookCheckoutDTO;
import com.pnudev.librarysystem.dto.BorrowingDTO;
import com.pnudev.librarysystem.dto.CreateBorrowingDTO;
import com.pnudev.librarysystem.security.UserDetailsImpl;
import com.pnudev.librarysystem.service.BorrowingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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

    @PreAuthorize("hasAuthority('CLIENT')")
    @PostMapping("/{id}/cancel")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancelReservation(
            @AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
            @PathVariable Long id) {
        borrowingService.cancelReservation(userDetailsImpl, id);
    }

    @PreAuthorize("hasAuthority('CLIENT')")
    @GetMapping("/my")
    public List<BorrowingDTO> findActiveUserBorrowings(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl) {
        return borrowingService.findActiveUserBorrowings(userDetailsImpl);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/{id}/renew")
    public void renewBook(@PathVariable Long id) {
        borrowingService.renewBook(id);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/{id}/checkout")
    public void checkoutBook(@PathVariable Long id, @RequestBody BookCheckoutDTO bookCheckoutDTO) {
        borrowingService.checkoutBook(id, bookCheckoutDTO);
    }


}
