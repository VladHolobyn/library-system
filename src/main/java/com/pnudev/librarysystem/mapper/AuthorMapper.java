package com.pnudev.librarysystem.mapper;

import com.pnudev.librarysystem.dto.author.AuthorDTO;
import com.pnudev.librarysystem.entity.Author;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;


@Mapper(componentModel = "spring")
public interface AuthorMapper {

    Author toEntity(AuthorDTO authorDTO);

    AuthorDTO toDTO(Author author);

    @Mapping(target = "id", ignore = true)
    void updateAuthorFromDTO(AuthorDTO authorDTO, @MappingTarget Author author);

}
