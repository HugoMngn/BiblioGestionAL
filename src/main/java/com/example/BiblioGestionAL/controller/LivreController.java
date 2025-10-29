package com.example.BiblioGestionAL.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.BiblioGestionAL.dto.LivreDTOs;
import com.example.BiblioGestionAL.service.LivreService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/livres")
public class LivreController {

    private final LivreService livreService;

    @Autowired
    public LivreController(LivreService livreService) {
        this.livreService = livreService;
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody LivreDTOs.CreateRequest req) {
        try {
            return ResponseEntity.ok(livreService.create(req));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<LivreDTOs.Response>> findAll() {
        return ResponseEntity.ok(livreService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(livreService.findById(id));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @GetMapping("/search/titre")
    public ResponseEntity<List<LivreDTOs.Response>> searchByTitre(@RequestParam String q) {
        return ResponseEntity.ok(livreService.searchByTitre(q));
    }

    @GetMapping("/search/auteur")
    public ResponseEntity<List<LivreDTOs.Response>> searchByAuteur(@RequestParam String q) {
        return ResponseEntity.ok(livreService.searchByAuteur(q));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            livreService.delete(id);
            return ResponseEntity.ok("Livre supprimé");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }
}