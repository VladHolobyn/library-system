package com.pnudev.librarysystem.entity;


import com.pnudev.librarysystem.enums.BorrowingStatus;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    @ManyToMany
    @JoinTable(
            name = "book_author",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "author_id")
    )
    private List<Author> authors;

    @ElementCollection
    @CollectionTable(name = "book_author", joinColumns = @JoinColumn(name = "book_id"))
    @Column(name = "author_id")
    private List<Long> authorIds;

    @ManyToMany
    @JoinTable(
            name = "book_category",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private List<Category> categories;

    @ElementCollection
    @CollectionTable(name = "book_category", joinColumns = @JoinColumn(name = "book_id"))
    @Column(name = "category_id")
    private List<Long> categoryIds;

    @OneToMany(mappedBy = "book")
    private List<Borrowing> borrowings;

    public long getAvailableCount() {
        long unavailableBooksCount = this.getBorrowings().stream().filter(borrowing -> borrowing.getStatus() != BorrowingStatus.RETURNED).count();

        return this.getQuantity() - unavailableBooksCount;
    }
}
