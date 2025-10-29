package com.example.BiblioGestionAL.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.BiblioGestionAL.dto.EmpruntDTOs;
import com.example.BiblioGestionAL.entity.Emprunt;
import com.example.BiblioGestionAL.entity.Livre;
import com.example.BiblioGestionAL.entity.User;
import com.example.BiblioGestionAL.repository.EmpruntRepository;
import com.example.BiblioGestionAL.repository.LivreRepository;
import com.example.BiblioGestionAL.repository.UserRepository;

@Service
public class EmpruntService {

    private final EmpruntRepository empruntRepository;
    private final LivreRepository livreRepository;
    private final UserRepository userRepository;

    @Autowired
    public EmpruntService(EmpruntRepository empruntRepository, LivreRepository livreRepository, UserRepository userRepository) {
        this.empruntRepository = empruntRepository;
        this.livreRepository = livreRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public EmpruntDTOs.Response emprunter(Long userId, EmpruntDTOs.CreateRequest req) {
        User utilisateur = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur non trouvé"));

        Livre livre = livreRepository.findById(req.getLivreId())
                .orElseThrow(() -> new IllegalArgumentException("Livre non trouvé"));

        if (livre.getStock() <= 0) {
            throw new IllegalArgumentException("Stock épuisé pour ce livre");
        }

        if (empruntRepository.existsByLivreAndStatut(livre, Emprunt.StatutEmprunt.EN_COURS)) {
            int enCours = (int) empruntRepository.findByLivre(livre).stream()
                    .filter(e -> e.getStatut() == Emprunt.StatutEmprunt.EN_COURS)
                    .count();
            if (enCours >= livre.getStock()) {
                throw new IllegalArgumentException("Tous les exemplaires sont déjà empruntés");
            }
        }

        LocalDate dateEmprunt = LocalDate.now();
        LocalDate dateRetourPrevue = dateEmprunt.plusDays(req.getDureeEmpruntJours());

        Emprunt emprunt = Emprunt.builder()
                .utilisateur(utilisateur)
                .livre(livre)
                .dateEmprunt(dateEmprunt)
                .dateRetourPrevue(dateRetourPrevue)
                .statut(Emprunt.StatutEmprunt.EN_COURS)
                .build();

        livre.setStock(livre.getStock() - 1);
        livreRepository.save(livre);

        return EmpruntDTOs.Response.fromEntity(empruntRepository.save(emprunt));
    }

    @Transactional
    public EmpruntDTOs.Response retourner(Long empruntId) {
        Emprunt emprunt = empruntRepository.findById(empruntId)
                .orElseThrow(() -> new IllegalArgumentException("Emprunt non trouvé"));

        if (emprunt.getStatut() != Emprunt.StatutEmprunt.EN_COURS) {
            throw new IllegalArgumentException("Cet emprunt n'est pas en cours");
        }

        LocalDate aujourdHui = LocalDate.now();
        emprunt.setDateRetourEffective(aujourdHui);
        emprunt.setStatut(Emprunt.StatutEmprunt.RETOURNE);

        if (aujourdHui.isAfter(emprunt.getDateRetourPrevue())) {
            emprunt.setStatut(Emprunt.StatutEmprunt.EN_RETARD);
        }

        Livre livre = emprunt.getLivre();
        livre.setStock(livre.getStock() + 1);
        livreRepository.save(livre);

        return EmpruntDTOs.Response.fromEntity(empruntRepository.save(emprunt));
    }

    public List<EmpruntDTOs.Response> findByUtilisateur(Long userId) {
        User utilisateur = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur non trouvé"));
        return empruntRepository.findByUtilisateur(utilisateur).stream()
                .map(EmpruntDTOs.Response::fromEntity)
                .collect(Collectors.toList());
    }

    public List<EmpruntDTOs.Response> findEnCours() {
        return empruntRepository.findByStatut(Emprunt.StatutEmprunt.EN_COURS).stream()
                .map(EmpruntDTOs.Response::fromEntity)
                .collect(Collectors.toList());
    }
}