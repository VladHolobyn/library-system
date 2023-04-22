package com.pnudev.librarysystem.service;

import com.pnudev.librarysystem.entity.Author;
import com.pnudev.librarysystem.entity.Book;
import com.pnudev.librarysystem.repository.AuthorRepository;
import com.pnudev.librarysystem.repository.BookRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class BookAuthorService {
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    public void addAuthorsToBook(Long bookId, List<Long> authorIds){
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book with id: %d not found".formatted(bookId)));

        List<Author> authors = authorRepository.findAllById(authorIds);
        authors.forEach(category-> category.getBooks().add(book));

        book.getAuthors().addAll(authors);
        bookRepository.save(book);
    }
}
