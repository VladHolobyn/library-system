package com.pnudev.librarysystem.service;

import com.pnudev.librarysystem.dto.CreateUserDTO;
import com.pnudev.librarysystem.dto.UpdateUserDTO;
import com.pnudev.librarysystem.dto.UserDTO;
import com.pnudev.librarysystem.entity.User;
import com.pnudev.librarysystem.enums.UserRole;
import com.pnudev.librarysystem.exception.NotUniqueException;
import com.pnudev.librarysystem.mapper.UserMapper;
import com.pnudev.librarysystem.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Value("${application.default-admin.email}")
    private String defaultAdminEmail;

    @Value("${application.default-admin.password}")
    private String defaultAdminPassword;

    @Override
    public UserDetails loadUserByUsername(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User with email: %s not found".formatted(email)));
        return userMapper.toUserDetails(user);
    }

    public void createAdminUserIfNotExists() {

        if (userRepository.existsByEmail(defaultAdminEmail)) {
            return;
        }

        User admin = User.builder()
                .firstName("admin")
                .lastName("admin")
                .email(defaultAdminEmail)
                .password(passwordEncoder.encode(defaultAdminPassword))
                .phoneNumber("0")
                .address("0")
                .role(UserRole.ADMIN)
                .build();

        userRepository.save(admin);
    }

    public UserDTO createUser(CreateUserDTO userDTO) {
        if (userRepository.existsByEmail(userDTO.getEmail())) {
            throw new NotUniqueException("User with such email already exists");
        }
        User user = userMapper.toEntity(userDTO);
        return userMapper.toDTO(userRepository.save(user));
    }

    public UserDTO updateUser(Long id, UpdateUserDTO userDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User with id:%d not found".formatted(id)));

        userMapper.updateUserFromDTO(userDTO, user);
        return userMapper.toDTO(userRepository.save(user));
    }

}
