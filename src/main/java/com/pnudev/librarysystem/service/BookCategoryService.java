package com.pnudev.librarysystem.service;

import com.pnudev.librarysystem.entity.Book;
import com.pnudev.librarysystem.entity.Category;
import com.pnudev.librarysystem.repository.BookRepository;
import com.pnudev.librarysystem.repository.CategoryRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class BookCategoryService {
    private final BookRepository bookRepository;
    private final CategoryRepository categoryRepository;

    public void addCategoriesToBook(Long bookId, List<Long> categoryIds){
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book with id: %d not found".formatted(bookId)));

        List<Category> categories = categoryRepository.findAllById(categoryIds);
        categories.forEach(category-> category.getBooks().add(book));

        book.getCategories().addAll(categories);
        bookRepository.save(book);
    }
}
