package com.pnudev.librarysystem.service;

import com.pnudev.librarysystem.dto.AuthorDTO;
import com.pnudev.librarysystem.dto.BookDTO;
import com.pnudev.librarysystem.dto.CategoryDTO;
import com.pnudev.librarysystem.dto.FormBookDTO;
import com.pnudev.librarysystem.entity.Author;
import com.pnudev.librarysystem.entity.Book;
import com.pnudev.librarysystem.entity.Category;
import com.pnudev.librarysystem.exception.DeleteFailedException;
import com.pnudev.librarysystem.mapper.AuthorMapper;
import com.pnudev.librarysystem.mapper.BookMapper;
import com.pnudev.librarysystem.mapper.CategoryMapper;
import com.pnudev.librarysystem.repository.BookRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;


@AllArgsConstructor
@Service
public class BookService {
    private final BookRepository bookRepository;
    private final AuthorService authorService;
    private final CategoryService categoryService;
    private final BookMapper bookMapper;
    private final AuthorMapper authorMapper;
    private final CategoryMapper categoryMapper;


    public BookDTO createBook(FormBookDTO formBookDTO)  {
        String fileContentType = formBookDTO.getCoverImage().getContentType();

        if (!Pattern.matches("image/jpe?g", fileContentType)) {
            throw new IllegalArgumentException("Wrong file type: only .jpeg/.jpg are allowed");
        }

        Book book;
        try {
            book = bookMapper.toEntity(formBookDTO);
        } catch (IOException e) {
            throw new IllegalArgumentException("IOException occurred in file: " + e.getMessage());
        }

        this.setBookAuthors(book, formBookDTO.getAuthorIds());
        this.setBookCategories(book,formBookDTO.getCategoryIds());


        return bookMapper.toDTO(bookRepository.save(book));
    }

    public BookDTO updateBook(Long id, FormBookDTO formBookDTO) {
        String fileContentType = formBookDTO.getCoverImage().getContentType();

        if (!Pattern.matches("image/jpe?g", fileContentType)) {
            throw new IllegalArgumentException("Wrong file type: only .jpeg/.jpg are allowed");
        }

        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Book with id: %d not found".formatted(id)));
        try {
            bookMapper.updateBookFromFormDTO(formBookDTO, book);
        } catch (IOException e) {
            throw new IllegalArgumentException("IOException occurred in file: " + e.getMessage());
        }

        this.setBookAuthors(book, formBookDTO.getAuthorIds());
        this.setBookCategories(book,formBookDTO.getCategoryIds());

        return bookMapper.toDTO(bookRepository.save(book));
    }

    private void setBookAuthors(Book book, List<Long> authorIds){
        List<AuthorDTO> authorDTOS = authorService.findAllById(authorIds);
        List<Author> authors = authorMapper.toEntity(authorDTOS);
        book.setAuthors(authors);
    }
    private void setBookCategories(Book book, List<Long> categoryIds){
        List<CategoryDTO> categoryDTOS = categoryService.findAllById(categoryIds);
        List<Category> categories = categoryMapper.toEntity(categoryDTOS);
        book.setCategories(categories);
    }

    public void deleteBookById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Book with id: %d not found".formatted(id)));

        if(!book.getBorrowings().isEmpty()) {
            throw new DeleteFailedException("Book with id: %d is used".formatted(id));
        }

        bookRepository.deleteById(id);
    }
//
//    public byte[] getBookImage(Long bookId) {
//        Book book = bookRepository.findById(bookId)
//                .orElseThrow(() -> new EntityNotFoundException("Book with id: %d not found".formatted(bookId)) );
//        return book.getCoverImage();
//    }

}
