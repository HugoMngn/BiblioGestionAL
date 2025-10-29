package com.example.BiblioGestionAL.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.BiblioGestionAL.entity.Emprunt;
import com.example.BiblioGestionAL.entity.Livre;
import com.example.BiblioGestionAL.entity.User;

public interface EmpruntRepository extends JpaRepository<Emprunt, Long> {

    List<Emprunt> findByUtilisateur(User utilisateur);

    List<Emprunt> findByLivre(Livre livre);

    List<Emprunt> findByStatut(Emprunt.StatutEmprunt statut);

    boolean existsByLivreAndStatut(Livre livre, Emprunt.StatutEmprunt statut);
}