package com.pnudev.librarysystem.mapper;

import com.pnudev.librarysystem.security.UserDetailsImpl;
import com.pnudev.librarysystem.entity.User;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDetailsImpl toUserDetails(User user);

}
