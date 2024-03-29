package com.pnudev.librarysystem.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pnudev.librarysystem.dto.error.ErrorDTO;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@AllArgsConstructor
@Component
public class JwtExceptionHandlerFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper;

    @Override
    public void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String message;

        try {
            filterChain.doFilter(request, response);
            return;
        } catch (SignatureException ex) {
            message = "Invalid JWT signature";
        } catch (MalformedJwtException ex) {
            message = "Invalid JWT token";
        } catch (ExpiredJwtException ex) {
            message = "Expired JWT token";
        } catch (UnsupportedJwtException ex) {
            message = "Unsupported JWT token";
        }

        ErrorDTO errorDTO = new ErrorDTO(message);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(objectMapper.writeValueAsString(errorDTO));

    }

}
