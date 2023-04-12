package com.pnudev.librarysystem.service;

import com.pnudev.librarysystem.dto.AuthorDTO;
import com.pnudev.librarysystem.mapper.AuthorMapper;
import com.pnudev.librarysystem.repository.AuthorRepository;
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
}
