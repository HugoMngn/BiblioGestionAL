package com.example.BiblioGestionAL.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.BiblioGestionAL.entity.Livre;

public interface LivreRepository extends JpaRepository<Livre, Long> {

    List<Livre> findByTitreContainingIgnoreCase(String titre);

    List<Livre> findByAuteurContainingIgnoreCase(String auteur);

    boolean existsByIsbn(String isbn);
}