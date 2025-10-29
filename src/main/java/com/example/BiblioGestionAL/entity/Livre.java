package com.example.BiblioGestionAL.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "livres")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Livre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Le titre est obligatoire")
    @Size(max = 255)
    @Column(nullable = false)
    private String titre;

    @NotBlank(message = "L'auteur est obligatoire")
    @Size(max = 255)
    @Column(nullable = false)
    private String auteur;

    @NotBlank(message = "L'ISBN est obligatoire")
    @Pattern(regexp = "^\\d{10}|\\d{13}$", message = "ISBN invalide (10 ou 13 chiffres)")
    @Column(unique = true, nullable = false, length = 13)
    private String isbn;

    @Min(value = 0, message = "L'année doit être positive")
    @Max(value = 2025, message = "L'année ne peut pas être dans le futur")
    @Column(name = "annee_publication")
    private int anneePublication;

    @Min(value = 0, message = "Le stock ne peut pas être négatif")
    @Column(nullable = false)
    private int stock;

    @Column(length = 1000)
    private String description;
}