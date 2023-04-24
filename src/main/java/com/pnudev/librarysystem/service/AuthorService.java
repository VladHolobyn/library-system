package com.pnudev.librarysystem.service;

import com.pnudev.librarysystem.dto.AuthorDTO;
import com.pnudev.librarysystem.entity.Author;
import com.pnudev.librarysystem.exception.DeleteFailedException;
import com.pnudev.librarysystem.mapper.AuthorMapper;
import com.pnudev.librarysystem.repository.AuthorRepository;
import com.pnudev.librarysystem.util.SpecificationUtils;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;



@AllArgsConstructor
@Service
public class AuthorService {

    private final AuthorRepository authorRepository;
    private final AuthorMapper authorMapper;

    public AuthorDTO addAuthor(AuthorDTO authorDTO) {
        Author author = authorMapper.toEntity(authorDTO);
        Author savedAuthor = authorRepository.save(author);
        return authorMapper.toDTO(savedAuthor);
    }

    public void deleteAuthor(Long id) {
        Author author = authorRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("Author with id: %d not found".formatted(id)));

        if(!author.getBooks().isEmpty()){
            throw new DeleteFailedException("Author with id: %d is used".formatted(id));
        }

        authorRepository.deleteById(id);
    }

    public AuthorDTO updateAuthor(Long id, AuthorDTO authorDTO) {
        Author author = authorRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("Author with id: %d not found".formatted(id)));

        authorMapper.updateAuthorFromDTO(authorDTO,author);

        return authorMapper.toDTO(authorRepository.save(author));
    }

    public Page<AuthorDTO> searchAuthorByParams(String firstName, String lastName, Pageable pageable) {
        Specification<Author> specification = Specification.where(null);

        if (firstName != null && !firstName.isEmpty()) {
            specification = specification.and(SpecificationUtils.<Author>fieldContainsIgnoreCase("firstName",firstName));
        }
        if (lastName != null && !lastName.isEmpty()){
            specification = specification.and(SpecificationUtils.<Author>fieldContainsIgnoreCase("lastName",lastName));
        }

        Page<Author> page = authorRepository.findAll(specification, pageable);
        return page.map(authorMapper::toDTO);
    }

}
