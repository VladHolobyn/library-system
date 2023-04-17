package com.pnudev.librarysystem.util;

import lombok.experimental.UtilityClass;
import org.springframework.data.jpa.domain.Specification;

@UtilityClass
public class SpecificationUtils{

    public static <T> Specification<T> fieldContainsIgnoreCase(String field, String value){
        return (root, cq, cb) -> cb.like(cb.lower(root.get(field)), "%" + value.toLowerCase() +"%");
    }
}
