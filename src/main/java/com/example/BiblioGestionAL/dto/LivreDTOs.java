package com.example.BiblioGestionAL.dto;

import com.example.BiblioGestionAL.entity.Livre;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class LivreDTOs {

    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    public static class CreateRequest {
        @NotBlank(message = "Le titre est obligatoire")
        @Size(max = 255)
        private String titre;

        @NotBlank(message = "L'auteur est obligatoire")
        @Size(max = 255)
        private String auteur;

        @NotBlank(message = "L'ISBN est obligatoire")
        @Pattern(regexp = "^\\d{10}|\\d{13}$", message = "ISBN invalide")
        private String isbn;

        @Min(0) @Max(2025)
        private int anneePublication;

        @Min(0)
        private int stock;

        private String description;
    }

    @Data @Builder
    public static class Response {
        private Long id;
        private String titre;
        private String auteur;
        private String isbn;
        private int anneePublication;
        private int stock;
        private String description;

        public static Response fromEntity(Livre livre) {
            return Response.builder()
                    .id(livre.getId())
                    .titre(livre.getTitre())
                    .auteur(livre.getAuteur())
                    .isbn(livre.getIsbn())
                    .anneePublication(livre.getAnneePublication())
                    .stock(livre.getStock())
                    .description(livre.getDescription())
                    .build();
        }
    }
}