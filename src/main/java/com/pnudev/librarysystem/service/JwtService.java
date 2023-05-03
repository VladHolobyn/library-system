package com.pnudev.librarysystem.service;

import com.pnudev.librarysystem.security.UserDetailsImpl;
import com.pnudev.librarysystem.enums.UserStatus;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtService {

    @Value("${application.secret-key}")
    private String secret;
    @Value("${application.token-expiration-time}")
    private int expirationTime;


    public String generateToken(String email, UserStatus role) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", role);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(email)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    public UserDetails extractUserDetails(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();

        return new UserDetailsImpl(
                claims.getSubject(),
                null,
                UserStatus.valueOf(claims.get("role", String.class))
        );
    }

}
