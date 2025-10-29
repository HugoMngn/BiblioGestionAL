package com.example.BiblioGestionAL.dto;

import java.time.LocalDate;

import com.example.BiblioGestionAL.entity.Emprunt;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class EmpruntDTOs {

    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    public static class CreateRequest {
        @NotNull(message = "L'ID du livre est requis")
        private Long livreId;

        @Min(value = 1, message = "Durée minimale : 1 jour")
        @Max(value = 30, message = "Durée maximale : 30 jours")
        private int dureeEmpruntJours;
    }

    @Data @Builder
    public static class Response {
        private Long id;
        private String utilisateurUsername;
        private String livreTitre;
        private String livreIsbn;
        private LocalDate dateEmprunt;
        private LocalDate dateRetourPrevue;
        private LocalDate dateRetourEffective;
        private Emprunt.StatutEmprunt statut;

        public static Response fromEntity(Emprunt emprunt) {
            return Response.builder()
                    .id(emprunt.getId())
                    .utilisateurUsername(emprunt.getUtilisateur().getUsername())
                    .livreTitre(emprunt.getLivre().getTitre())
                    .livreIsbn(emprunt.getLivre().getIsbn())
                    .dateEmprunt(emprunt.getDateEmprunt())
                    .dateRetourPrevue(emprunt.getDateRetourPrevue())
                    .dateRetourEffective(emprunt.getDateRetourEffective())
                    .statut(emprunt.getStatut())
                    .build();
        }
    }
}