package com.pnudev.librarysystem.mapper;

import com.pnudev.librarysystem.dto.BookDTO;
import com.pnudev.librarysystem.dto.FormBookDTO;
import com.pnudev.librarysystem.entity.Book;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.io.IOException;

@Mapper(componentModel = "spring",
uses = {CategoryMapper.class, AuthorMapper.class})
public interface BookMapper {

    @Mapping(target = "coverImage", expression = "java(formBookDTO.getCoverImage().getBytes())")
    Book toEntity(FormBookDTO formBookDTO) throws IOException;

    @Mapping(
            target = "coverImageUrl",
            expression = "java(\"/books/\" + book.getId()+\"/image\")",
            resultType = String.class
    )
    BookDTO toDTO(Book book);

    @InheritConfiguration
    void updateBookFromFormDTO(FormBookDTO formBookDTO, @MappingTarget Book book) throws IOException;
}
