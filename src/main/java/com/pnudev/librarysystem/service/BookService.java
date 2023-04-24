package com.pnudev.librarysystem.service;

import com.pnudev.librarysystem.dto.BookDTO;
import com.pnudev.librarysystem.dto.RequestBookDTO;
import com.pnudev.librarysystem.entity.Book;
import com.pnudev.librarysystem.exception.DeleteFailedException;
import com.pnudev.librarysystem.exception.EmptyFileException;
import com.pnudev.librarysystem.exception.FileWrongTypeException;
import com.pnudev.librarysystem.exception.IOErrorInFileException;
import com.pnudev.librarysystem.mapper.BookMapper;
import com.pnudev.librarysystem.repository.BookRepository;
import com.pnudev.librarysystem.util.SpecificationUtils;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
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

    @Value("${file.upload-dir}/books")
    private String uploadDir;

    @Value("${file.image-types}")
    private List<String> imageTypes;

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;


    public Long createBook(RequestBookDTO requestBookDTO)  {
        Book book = bookMapper.toEntity(requestBookDTO);
        Book savedBook = bookRepository.save(book);
        return savedBook.getId();
    }

    public BookDTO updateBook(Long id, RequestBookDTO requestBookDTO) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Book with id: %d not found".formatted(id)));

        bookMapper.updateBookFromRequestDTO(requestBookDTO, book);

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

    public Page<BookDTO> searchBook(String title, String authorLastName, String categoryName, Pageable pageable) {
        Specification<Book> specification = Specification.where(null);

        if (title != null && !title.isEmpty()) {
            specification = specification.and(SpecificationUtils.<Book>fieldContainsIgnoreCase("title", title));
        }
        if (authorLastName != null && !authorLastName.isEmpty()){
            specification = specification.and(
                    SpecificationUtils.<Book>joinTableFieldContainsIgnoreCase("authors", "lastName", authorLastName)
            );
        }
        if (categoryName != null && !categoryName.isEmpty()){
            specification = specification.and(
                    SpecificationUtils.<Book>joinTableFieldContainsIgnoreCase("categories", "name", categoryName)
            );
        }

        Page<Book> page = bookRepository.findAll(specification, pageable);
        return page.map(bookMapper::toDTO);
    }

    public BookDTO getBookById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Book with id: %d not found".formatted(id)));
        return bookMapper.toDTO(book);
    }

    public String uploadCoverImage(MultipartFile file) {
        if(file.isEmpty()) {
            throw new EmptyFileException("File cannot be empty");
        }

        String originalFileName = file.getOriginalFilename();
        String fileExtension = originalFileName.substring(originalFileName.lastIndexOf(".")+1);

        if (!imageTypes.contains(fileExtension)) {
            throw new FileWrongTypeException(imageTypes);
        }

        Path uploadPath= Paths.get(uploadDir);
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
            Files.copy(file.getInputStream(),imagePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new IOErrorInFileException("IOException occurred while saving file to filesystem: " + e.getMessage());
        }

        return filename;
    }

    public byte[] getBookImage(Long bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book with id: %d not found".formatted(bookId)));
        try {
            return Files.readAllBytes(Paths.get(uploadDir).resolve(book.getCoverImage()));
        } catch (IOException e) {
            throw new IOErrorInFileException("IOException occurred while reading file: " + e.getMessage());
        }
    }

}
