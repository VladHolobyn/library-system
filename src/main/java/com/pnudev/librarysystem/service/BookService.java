package com.pnudev.librarysystem.service;

import com.pnudev.librarysystem.dto.AuthorDTO;
import com.pnudev.librarysystem.dto.BookDTO;
import com.pnudev.librarysystem.dto.CategoryDTO;
import com.pnudev.librarysystem.dto.FormBookDTO;
import com.pnudev.librarysystem.entity.Author;
import com.pnudev.librarysystem.entity.Book;
import com.pnudev.librarysystem.entity.Category;
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

        Book book = null;
        try {
            book = bookMapper.toEntity(formBookDTO);
        } catch (IOException e) {
            throw new IllegalArgumentException("IOException occurred in file: " + e.getMessage());
        }

        List<AuthorDTO> authorDTOS = authorService.findAllById(formBookDTO.getAuthorIds());
        List<Author> authors = authorMapper.toEntity(authorDTOS);
        book.setAuthors(authors);

        List<CategoryDTO> categoryDTOS = categoryService.findAllById(formBookDTO.getCategoryIds());
        List<Category> categories = categoryMapper.toEntity(categoryDTOS);
        book.setCategories(categories);

        return bookMapper.toDTO(bookRepository.save(book));
    }
//
//    public byte[] getBookImage(Long bookId) {
//        Book book = bookRepository.findById(bookId)
//                .orElseThrow(() -> new EntityNotFoundException("Book with id: %d not found".formatted(bookId)) );
//        return book.getCoverImage();
//    }

}
