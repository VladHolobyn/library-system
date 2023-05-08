package com.pnudev.librarysystem.service;

import com.pnudev.librarysystem.dto.AuthorDTO;
import com.pnudev.librarysystem.entity.Author;
import com.pnudev.librarysystem.exception.OperationFailedException;
import com.pnudev.librarysystem.mapper.AuthorMapper;
import com.pnudev.librarysystem.repository.AuthorRepository;
import com.pnudev.librarysystem.specification.AuthorSpecificationBuilder;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;


@AllArgsConstructor
@Service
public class AuthorService {

    private final AuthorRepository authorRepository;
    private final AuthorMapper authorMapper;
    private final AuthorSpecificationBuilder authorSpecificationBuilder;

    public AuthorDTO addAuthor(AuthorDTO authorDTO) {
        Author author = authorMapper.toEntity(authorDTO);
        Author savedAuthor = authorRepository.save(author);
        return authorMapper.toDTO(savedAuthor);
    }

    public void deleteAuthor(Long id) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Author with id: %d not found".formatted(id)));

        if (!author.getBooks().isEmpty()) {
            throw new OperationFailedException("Author with id: %d is used".formatted(id));
        }

        authorRepository.deleteById(id);
    }

    public AuthorDTO updateAuthor(Long id, AuthorDTO authorDTO) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Author with id: %d not found".formatted(id)));

        authorMapper.updateAuthorFromDTO(authorDTO, author);

        return authorMapper.toDTO(authorRepository.save(author));
    }

    public Page<AuthorDTO> searchAuthorByParams(String firstName, String lastName, Pageable pageable) {
        Specification<Author> specification = Specification.where(null);

        if (!StringUtils.isEmpty(firstName)) {
            specification = specification.and(authorSpecificationBuilder.fieldContainsIgnoreCase("firstName", firstName));
        }
        if (!StringUtils.isEmpty(lastName)) {
            specification = specification.and(authorSpecificationBuilder.fieldContainsIgnoreCase("lastName", lastName));
        }

        Page<Author> page = authorRepository.findAll(specification, pageable);
        return page.map(authorMapper::toDTO);
    }

}
