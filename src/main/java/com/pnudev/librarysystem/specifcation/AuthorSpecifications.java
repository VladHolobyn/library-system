package com.pnudev.librarysystem.specifcation;

import com.pnudev.librarysystem.entity.Author;
import org.springframework.data.jpa.domain.Specification;

public class AuthorSpecifications {
    private AuthorSpecifications(){}
    public static Specification<Author> fieldContainsIgnoreCase(String field, String firstname){
        return (author, cq, cb) -> cb.like(cb.lower(author.get(field)), "%" + firstname.toLowerCase() +"%");
    }
}
