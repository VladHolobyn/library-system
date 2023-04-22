package com.pnudev.librarysystem.service;

import com.pnudev.librarysystem.dto.BookDTO;
import com.pnudev.librarysystem.dto.FormBookDTO;
import com.pnudev.librarysystem.entity.Book;
import com.pnudev.librarysystem.mapper.BookMapper;
import com.pnudev.librarysystem.repository.BookRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;


@AllArgsConstructor
@Service
public class BookService {
    private final BookRepository bookRepository;
    private final BookMapper mapper;
    private final BookCategoryService bookCategoryService;
    private final BookAuthorService bookAuthorService;


    public BookDTO addBook(FormBookDTO formBookDTO){
        Book book = mapper.toEntity(formBookDTO);
        Book savedBook = bookRepository.save(book);

        bookCategoryService.addCategoriesToBook(savedBook.getId(), formBookDTO.getCategoryIds());
        bookAuthorService.addAuthorsToBook(savedBook.getId(), formBookDTO.getAuthorIds());

        return mapper.toDTO(savedBook);
    }

}
