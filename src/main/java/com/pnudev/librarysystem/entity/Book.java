package com.pnudev.librarysystem.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.LinkedList;
import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "book")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String title;

    @NotBlank
    private String coverImage;

    @NotBlank
    private String description;

    @NotNull
    private Integer publicationYear;

    @NotNull
    private Integer quantity;

    @ManyToMany(mappedBy = "books")
    private List<Author> authors = new LinkedList<>();

    @ManyToMany(mappedBy = "books")
    private List<Category> categories = new LinkedList<>();

    @OneToMany(mappedBy = "book")
    private List<Borrowing> borrowings = new LinkedList<>();

}
