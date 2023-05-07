package com.pnudev.librarysystem.mapper;

import com.pnudev.librarysystem.dto.CreateUserDTO;
import com.pnudev.librarysystem.security.UserDetailsImpl;
import com.pnudev.librarysystem.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

@Mapper(componentModel = "spring")
public abstract class UserMapper {
    @Autowired
    protected PasswordEncoder passwordEncoder;

    public abstract UserDetailsImpl toUserDetails(User user);

    @Mapping(target = "password", expression = "java(passwordEncoder.encode(createUserDTO.getPassword()))")
    public abstract User toEntity(CreateUserDTO createUserDTO);
}
