package com.pnudev.librarysystem.service;

import com.pnudev.librarysystem.dto.BookDTO;
import com.pnudev.librarysystem.dto.RequestBookDTO;
import com.pnudev.librarysystem.entity.Book;
import com.pnudev.librarysystem.exception.DeleteFailedException;
import com.pnudev.librarysystem.mapper.AuthorMapper;
import com.pnudev.librarysystem.mapper.BookMapper;
import com.pnudev.librarysystem.mapper.CategoryMapper;
import com.pnudev.librarysystem.repository.BookRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.regex.Pattern;


@AllArgsConstructor
@Service
public class BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;


    public Long createBook(RequestBookDTO requestBookDTO)  {

        String fileContentType = requestBookDTO.getCoverImage().getContentType();
        if (!Pattern.matches("image/jpe?g", fileContentType)) {
            throw new IllegalArgumentException("Wrong file type: only .jpeg/.jpg are allowed");
        }

        Book book;
        try {
            book = bookMapper.toEntity(requestBookDTO);
        } catch (IOException e) {
            throw new IllegalArgumentException("IOException occurred in file: " + e.getMessage());
        }

        return bookRepository.save(book).getId();
    }

    public BookDTO updateBook(Long id, RequestBookDTO requestBookDTO) {

        String fileContentType = requestBookDTO.getCoverImage().getContentType();
        if (!Pattern.matches("image/jpe?g", fileContentType)) {
            throw new IllegalArgumentException("Wrong file type: only .jpeg/.jpg are allowed");
        }

        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Book with id: %d not found".formatted(id)));

        try {
            bookMapper.updateBookFromRequestDTO(requestBookDTO, book);
        } catch (IOException e) {
            throw new IllegalArgumentException("IOException occurred in file: " + e.getMessage());
        }

        return bookMapper.toDTO(bookRepository.save(book));
    }

    public void deleteBookById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Book with id: %d not found".formatted(id)));

        if(!book.getBorrowings().isEmpty()) {
            throw new DeleteFailedException("Book with id: %d is used".formatted(id));
        }

        bookRepository.deleteById(id);
    }

}
