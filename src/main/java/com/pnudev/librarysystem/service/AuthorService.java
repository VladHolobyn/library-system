package com.pnudev.librarysystem.service;

import com.pnudev.librarysystem.dto.AuthorDTO;
import com.pnudev.librarysystem.entity.Author;
import com.pnudev.librarysystem.exception.DeleteFailedException;
import com.pnudev.librarysystem.mapper.AuthorMapper;
import com.pnudev.librarysystem.repository.AuthorRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;

import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class AuthorService {

    private final AuthorRepository authorRepository;
    private final AuthorMapper authorMapper;

    public AuthorDTO addAuthor(AuthorDTO authorDTO) {
        return authorMapper.toDTO(authorRepository.save(authorMapper.toEntity(authorDTO)));
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
}
