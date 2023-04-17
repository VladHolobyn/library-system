package com.pnudev.librarysystem.util;

import org.springframework.data.jpa.domain.Specification;

public class SpecificationUtils{
    private SpecificationUtils(){}

    public static <T> Specification<T> fieldContainsIgnoreCase(String field, String value){
        return (root, cq, cb) -> cb.like(cb.lower(root.get(field)), "%" + value.toLowerCase() +"%");
    }
}
