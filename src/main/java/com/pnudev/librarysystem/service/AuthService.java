package com.pnudev.librarysystem.service;

import com.pnudev.librarysystem.dto.auth.AuthenticationResponseDTO;
import com.pnudev.librarysystem.dto.auth.LoginDTO;
import com.pnudev.librarysystem.enums.UserRole;
import com.pnudev.librarysystem.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthenticationResponseDTO login(LoginDTO loginDTO) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword())
        );

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        String email = userDetails.getUsername();
        Long userID = userDetails.getId();
        UserRole role = UserRole.valueOf(
                userDetails.getAuthorities().stream().map(Object::toString).toList().get(0));

        String jwt = jwtService.generateToken(email, userID, role);

        return new AuthenticationResponseDTO(jwt);
    }

}
