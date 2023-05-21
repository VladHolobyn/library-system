package com.pnudev.librarysystem.mapper;

import com.pnudev.librarysystem.dto.book.BookDTO;
import com.pnudev.librarysystem.dto.book.RequestBookDTO;
import com.pnudev.librarysystem.entity.Book;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;


@Mapper(componentModel = "spring",
        uses = {CategoryMapper.class, AuthorMapper.class})
public interface BookMapper {

    @Mapping(target = "coverImage", source = "coverImageName")
    Book toEntity(RequestBookDTO requestBookDTO);

    @Mapping(
            target = "coverImageUrl",
            expression = "java(\"/books/%d/image\".formatted(book.getId()))"
    )
    @Mapping(
            target = "available",
            expression = "java(book.getAvailableCount())"
    )
    BookDTO toDTO(Book book);

    @InheritConfiguration
    void updateBookFromRequestDTO(RequestBookDTO requestBookDTO, @MappingTarget Book book);
}
