package com.pnudev.librarysystem.controller;

import com.pnudev.librarysystem.dto.auth.AuthenticationResponseDTO;
import com.pnudev.librarysystem.dto.auth.LoginDTO;
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
    public AuthenticationResponseDTO login(@Valid @RequestBody LoginDTO loginDTO) {
        return authService.login(loginDTO);
    }

}
