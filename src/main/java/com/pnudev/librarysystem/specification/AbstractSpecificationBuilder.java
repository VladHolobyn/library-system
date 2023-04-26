package com.pnudev.librarysystem.specification;

import org.springframework.data.jpa.domain.Specification;


public abstract class AbstractSpecificationBuilder<T> {

    public Specification<T> fieldContainsIgnoreCase(String field, String value) {
        return (root, cq, cb) -> cb.like(cb.lower(root.get(field)), "%" + value.toLowerCase() + "%");
    }

    public Specification<T> joinTableFieldContainsIgnoreCase(String mappedBy, String field, String value) {
        return (root, cq, cb) -> {
            return cb.like(cb.lower(root.join(mappedBy).get(field)), "%" + value.toLowerCase() + "%");
        };
    }

}
