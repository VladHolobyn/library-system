package com.pnudev.librarysystem.service;

import com.pnudev.librarysystem.dto.AuthenticationResponseDTO;
import com.pnudev.librarysystem.dto.LoginUserDTO;
import com.pnudev.librarysystem.enums.UserRole;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
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

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        String email = userDetails.getUsername();
        UserRole role = UserRole.valueOf(
                userDetails.getAuthorities().stream().map(Object::toString).toList().get(0));

        String jwt = jwtService.generateToken(email, role);

        return new AuthenticationResponseDTO(jwt);
    }

}
