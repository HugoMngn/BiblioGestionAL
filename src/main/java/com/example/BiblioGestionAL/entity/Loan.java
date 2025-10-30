package com.example.BiblioGestionAL.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

// Loan Entity
@Entity
@Getter 
@Setter 
@NoArgsConstructor 
@AllArgsConstructor 
@Builder
public class Loan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relations
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnoreProperties({"password", "roles"})
    private User user;

    // Relations
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @Column(name = "loan_date")
    private LocalDate loanDate;

    @Column(name = "due_date")
    private LocalDate dueDate;

    @Column(name = "return_date")
    private LocalDate returnDate;

    @Column(nullable = false)
    @Builder.Default
    private Boolean approved = false;

    // Méthodes pour la sérialisation JSON
    @JsonProperty("username")
    public String getUsername() {
        return user != null ? user.getUsername() : null;
    }

    @JsonProperty("bookId")
    public Long getBookId() {
        return book != null ? book.getId() : null;
    }

    @JsonProperty("bookTitle")
    public String getBookTitle() {
        return book != null ? book.getTitle() : null;
    }

    @JsonProperty("bookAuthor")
    public String getBookAuthor() {
        return book != null ? book.getAuthor() : null;
    }

    @JsonProperty("bookGenre")
    public String getBookGenre() {
        return book != null ? book.getGenre() : null;
    }
}