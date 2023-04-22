package com.pnudev.librarysystem.mapper;

import com.pnudev.librarysystem.dto.BookDTO;
import com.pnudev.librarysystem.dto.FormBookDTO;
import com.pnudev.librarysystem.entity.Book;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring",
uses = {CategoryMapper.class, AuthorMapper.class})
public interface BookMapper {

    @Mapping(source = "coverImageUrl", target = "coverImage")
    Book toEntity(FormBookDTO bookDTO);

    @InheritConfiguration
    BookDTO toDTO(Book book);

}
