package com.pnudev.librarysystem.controller;

import com.pnudev.librarysystem.dto.AuthenticationResponseDTO;
import com.pnudev.librarysystem.dto.LoginUserDTO;
import com.pnudev.librarysystem.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RequiredArgsConstructor
@RestController
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public AuthenticationResponseDTO login(@Valid @RequestBody LoginUserDTO loginUser) {
        return authService.login(loginUser);
    }

}
