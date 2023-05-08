package com.pnudev.librarysystem.service;

import com.pnudev.librarysystem.dto.BookDTO;
import com.pnudev.librarysystem.dto.RequestBookDTO;
import com.pnudev.librarysystem.entity.Book;
import com.pnudev.librarysystem.enums.BorrowingStatus;
import com.pnudev.librarysystem.exception.OperationFailedException;
import com.pnudev.librarysystem.exception.EmptyFileException;
import com.pnudev.librarysystem.exception.FileWrongTypeException;
import com.pnudev.librarysystem.exception.IOErrorInFileException;
import com.pnudev.librarysystem.mapper.BookMapper;
import com.pnudev.librarysystem.repository.BookRepository;
import com.pnudev.librarysystem.specification.BookSpecificationBuilder;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;


@RequiredArgsConstructor
@Service
public class BookService {
    private static final String BOOK_NOT_FOUND_MSG_FORMAT = "Book with id: %d not found";

    @Value("${file.upload-dir}/books")
    private String uploadDir;

    @Value("${file.image-types}")
    private List<String> imageTypes;

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final BookSpecificationBuilder bookSpecificationBuilder;


    public Long createBook(RequestBookDTO requestBookDTO) {
        Book book = bookMapper.toEntity(requestBookDTO);
        Book savedBook = bookRepository.save(book);
        return savedBook.getId();
    }

    public BookDTO updateBook(Long id, RequestBookDTO requestBookDTO) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(BOOK_NOT_FOUND_MSG_FORMAT.formatted(id)));

        bookMapper.updateBookFromRequestDTO(requestBookDTO, book);

        return bookMapper.toDTO(bookRepository.save(book));
    }

    public void deleteBookById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(BOOK_NOT_FOUND_MSG_FORMAT.formatted(id)));

        if (!book.getBorrowings().isEmpty()) {
            throw new OperationFailedException("Book with id: %d is used".formatted(id));
        }

        bookRepository.deleteById(id);
    }

    public Page<BookDTO> searchBook(String title, String authorLastName, String categoryName, Pageable pageable) {
        Specification<Book> specification = Specification.where(null);

        if (!StringUtils.isEmpty(title)) {
            specification = specification.and(bookSpecificationBuilder.fieldContainsIgnoreCase("title", title));
        }
        if (!StringUtils.isEmpty(authorLastName)) {
            specification = specification.and(
                    bookSpecificationBuilder.joinTableFieldContainsIgnoreCase("authors", "lastName", authorLastName)
            );
        }
        if (!StringUtils.isEmpty(categoryName)) {
            specification = specification.and(
                    bookSpecificationBuilder.joinTableFieldContainsIgnoreCase("categories", "name", categoryName)
            );
        }

        Page<Book> page = bookRepository.findAll(specification, pageable);
        return page.map(bookMapper::toDTO);
    }

    public BookDTO getBookById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(BOOK_NOT_FOUND_MSG_FORMAT.formatted(id)));
        return bookMapper.toDTO(book);
    }

    public String uploadCoverImage(MultipartFile file) {
        if (file.isEmpty()) {
            throw new EmptyFileException("File cannot be empty");
        }

        String fileExtension = StringUtils.substringAfterLast(file.getOriginalFilename(), ".");
        if (!imageTypes.contains(fileExtension)) {
            throw new FileWrongTypeException(imageTypes);
        }

        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            try {
                Files.createDirectory(uploadPath);
            } catch (IOException e) {
                throw new IOErrorInFileException("Cannot create directory: " + e.getMessage());
            }
        }

        String filename = UUID.randomUUID() + "." + fileExtension;
        Path imagePath = uploadPath.resolve(filename);

        try {
            Files.copy(file.getInputStream(), imagePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new IOErrorInFileException("IOException occurred while saving file to filesystem: " + e.getMessage());
        }

        return filename;
    }

    public byte[] getBookImage(Long bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException(BOOK_NOT_FOUND_MSG_FORMAT.formatted(bookId)));
        try {
            return Files.readAllBytes(Paths.get(uploadDir).resolve(book.getCoverImage()));
        } catch (IOException e) {
            throw new IOErrorInFileException("IOException occurred while reading file: " + e.getMessage());
        }
    }

    public boolean isBookAvailable(Long bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException(BOOK_NOT_FOUND_MSG_FORMAT.formatted(bookId)));

        long unavailableBooksCount = book.getBorrowings().stream().filter(borrowing -> borrowing.getStatus() != BorrowingStatus.RETURNED).count();

        return book.getQuantity() - unavailableBooksCount > 0;
    }
}
