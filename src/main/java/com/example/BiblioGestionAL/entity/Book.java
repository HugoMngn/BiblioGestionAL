package com.example.BiblioGestionAL.entity;

import jakarta.persistence.*;
import lombok.*;

// Book Entity
@Entity
@Table(name = "book")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Book {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Book details
    private String title;
    private String author;
    private String genre;
    private String isbn;
    
    @Builder.Default
    private boolean available = true;

    @Column(length = 2000)
    private String description;
}
