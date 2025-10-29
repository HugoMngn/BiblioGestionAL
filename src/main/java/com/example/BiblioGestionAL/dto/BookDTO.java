package com.example.BiblioGestionAL.dto;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class BookDTO {
    private Long id;
    private String title;
    private String author;
    private String genre;
    private String isbn;
    private String description;
}
