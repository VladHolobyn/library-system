package com.pnudev.librarysystem.mapper;

import com.pnudev.librarysystem.dto.BookDTO;
import com.pnudev.librarysystem.dto.RequestBookDTO;
import com.pnudev.librarysystem.entity.Book;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.io.IOException;

@Mapper(componentModel = "spring",
uses = {CategoryMapper.class, AuthorMapper.class})
public interface BookMapper {

    @Mapping(target = "coverImage", expression = "java(requestBookDTO.getCoverImage().getBytes())")
    Book toEntity(RequestBookDTO requestBookDTO) throws IOException;

    @Mapping(
            target = "coverImageUrl",
            expression = "java(\"/books/\" + book.getId()+\"/image\")",
            resultType = String.class
    )
    BookDTO toDTO(Book book);


    @InheritConfiguration
    void updateBookFromRequestDTO(RequestBookDTO requestBookDTO, @MappingTarget Book book) throws IOException;
}
