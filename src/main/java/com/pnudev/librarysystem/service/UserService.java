package com.pnudev.librarysystem.service;

import com.pnudev.librarysystem.entity.User;
import com.pnudev.librarysystem.mapper.UserMapper;
import com.pnudev.librarysystem.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;


@AllArgsConstructor
@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User with email: %s not found".formatted(email)));
        return userMapper.toUserDetails(user);
    }

}
