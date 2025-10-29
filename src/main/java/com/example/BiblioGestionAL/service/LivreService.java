package com.example.BiblioGestionAL.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.BiblioGestionAL.dto.LivreDTOs;
import com.example.BiblioGestionAL.entity.Livre;
import com.example.BiblioGestionAL.repository.LivreRepository;

@Service
public class LivreService {

    private final LivreRepository livreRepository;

    @Autowired
    public LivreService(LivreRepository livreRepository) {
        this.livreRepository = livreRepository;
    }

    public LivreDTOs.Response create(LivreDTOs.CreateRequest req) {
        if (livreRepository.existsByIsbn(req.getIsbn())) {
            throw new IllegalArgumentException("Un livre avec cet ISBN existe déjà");
        }

        Livre livre = Livre.builder()
                .titre(req.getTitre())
                .auteur(req.getAuteur())
                .isbn(req.getIsbn())
                .anneePublication(req.getAnneePublication())
                .stock(req.getStock())
                .description(req.getDescription())
                .build();

        return LivreDTOs.Response.fromEntity(livreRepository.save(livre));
    }

    public List<LivreDTOs.Response> findAll() {
        return livreRepository.findAll().stream()
                .map(LivreDTOs.Response::fromEntity)
                .collect(Collectors.toList());
    }

    public LivreDTOs.Response findById(Long id) {
        Livre livre = livreRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Livre non trouvé"));
        return LivreDTOs.Response.fromEntity(livre);
    }

    public List<LivreDTOs.Response> searchByTitre(String titre) {
        return livreRepository.findByTitreContainingIgnoreCase(titre).stream()
                .map(LivreDTOs.Response::fromEntity)
                .collect(Collectors.toList());
    }

    public List<LivreDTOs.Response> searchByAuteur(String auteur) {
        return livreRepository.findByAuteurContainingIgnoreCase(auteur).stream()
                .map(LivreDTOs.Response::fromEntity)
                .collect(Collectors.toList());
    }

    public void delete(Long id) {
        if (!livreRepository.existsById(id)) {
            throw new IllegalArgumentException("Livre non trouvé");
        }
        livreRepository.deleteById(id);
    }
}