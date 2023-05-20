package com.pnudev.librarysystem.mapper;

import com.pnudev.librarysystem.dto.user.CreateUserDTO;
import com.pnudev.librarysystem.dto.user.UpdateUserDTO;
import com.pnudev.librarysystem.dto.user.UserDTO;
import com.pnudev.librarysystem.security.UserDetailsImpl;
import com.pnudev.librarysystem.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

@Mapper(componentModel = "spring")
public abstract class UserMapper {
    @Autowired
    protected PasswordEncoder passwordEncoder;

    public abstract UserDetailsImpl toUserDetails(User user);

    @Mapping(target = "password", expression = "java(passwordEncoder.encode(userDTO.getPassword()))")
    public abstract User toEntity(CreateUserDTO userDTO);

    public abstract UserDTO toDTO(User user);

    @Mapping(
            target = "password",
            expression = "java(userDTO.getPassword() == null ? user.getPassword() : passwordEncoder.encode(userDTO.getPassword()))"
    )
    public abstract void updateUserFromDTO(UpdateUserDTO userDTO, @MappingTarget User user);
}
