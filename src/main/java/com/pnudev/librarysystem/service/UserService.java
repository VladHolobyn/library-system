package com.pnudev.librarysystem.service;

import com.pnudev.librarysystem.dto.user.CreateUserDTO;
import com.pnudev.librarysystem.dto.user.UpdateUserDTO;
import com.pnudev.librarysystem.dto.user.UserDTO;
import com.pnudev.librarysystem.entity.User;
import com.pnudev.librarysystem.enums.BorrowingStatus;
import com.pnudev.librarysystem.enums.UserRole;
import com.pnudev.librarysystem.exception.NotUniqueException;
import com.pnudev.librarysystem.mapper.UserMapper;
import com.pnudev.librarysystem.repository.UserRepository;
import com.pnudev.librarysystem.specification.UserSpecificationBuilder;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class UserService implements UserDetailsService {
    private static final String USER_NOT_FOUND_MSG_FORMAT = "User with id: %d not found";

    private final UserRepository userRepository;
    private final UserSpecificationBuilder userSpecificationBuilder;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Value("${application.default-admin.email}")
    private String defaultAdminEmail;

    @Value("${application.default-admin.password}")
    private String defaultAdminPassword;

    @Value("${application.reservation.max-count-per-user}")
    private Integer maxReservationCount;

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

    public void createUser(CreateUserDTO userDTO) {
        if (userRepository.existsByEmail(userDTO.getEmail())) {
            throw new NotUniqueException("User with this email already exists");
        }
        User user = userMapper.toEntity(userDTO);
        userRepository.save(user);
    }

    public void updateUser(Long id, UpdateUserDTO userDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(USER_NOT_FOUND_MSG_FORMAT.formatted(id)));

        userMapper.updateUserFromDTO(userDTO, user);
        userRepository.save(user);
    }

    public Page<UserDTO> searchUser(String lastName, String email, String phoneNumber, Pageable pageable) {
        Specification<User> specification = Specification.where(null);

        if (StringUtils.isNotEmpty(lastName)) {
            specification = specification.and(userSpecificationBuilder.fieldContainsIgnoreCase("lastName", lastName));
        }
        if (StringUtils.isNotEmpty(email)) {
            specification = specification.and(userSpecificationBuilder.fieldContainsIgnoreCase("email", email));
        }
        if (StringUtils.isNotEmpty(phoneNumber)) {
            specification = specification.and(userSpecificationBuilder.fieldContainsIgnoreCase("phoneNumber", phoneNumber));
        }

        Page<User> page = userRepository.findAll(specification, pageable);
        return page.map(userMapper::toDTO);
    }

    public boolean canUserReserve(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(USER_NOT_FOUND_MSG_FORMAT.formatted(id)));
        long reservationCount = user.getBorrowedBooks().stream().filter(borrowing -> borrowing.getStatus() == BorrowingStatus.RESERVED).count();

        return maxReservationCount - reservationCount > 0;
    }
}
