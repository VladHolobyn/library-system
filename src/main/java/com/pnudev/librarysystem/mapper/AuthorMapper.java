package com.pnudev.librarysystem.mapper;

import com.pnudev.librarysystem.dto.AuthorDTO;
import com.pnudev.librarysystem.entity.Author;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface AuthorMapper {

    Author toEntity(AuthorDTO categoryDTO);

    AuthorDTO toDTO(Author author);

    void updateAuthorFromDTO(AuthorDTO authorDTO, @MappingTarget Author category);

}
