package com.pnudev.librarysystem.service;

import com.pnudev.librarysystem.dto.AuthenticationResponseDTO;
import com.pnudev.librarysystem.dto.LoginUserDTO;
import com.pnudev.librarysystem.enums.UserRole;
import com.pnudev.librarysystem.security.UserDetailsImpl;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;


@AllArgsConstructor
@Service
public class AuthService {

    private AuthenticationManager authenticationManager;
    private JwtService jwtService;


    public AuthenticationResponseDTO login(LoginUserDTO loginUser) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginUser.getEmail(), loginUser.getPassword())
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
