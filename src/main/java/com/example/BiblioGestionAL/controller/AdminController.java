package com.example.BiblioGestionAL.controller;

import com.example.BiblioGestionAL.entity.Role;
import com.example.BiblioGestionAL.entity.User;
import com.example.BiblioGestionAL.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

/**
 * Endpoints pour admin: créer bibliothécaires, lister utilisateurs, promouvoir...
 */
@RestController
@RequestMapping("/api/admin")
public class AdminController {
    private final UserService userService;
    
    @Autowired
    public AdminController(UserService userService) { 
        this.userService = userService; 
    }

    /**
     * Créer un bibliothécaire (pas un admin!)
     */
    @PostMapping("/create-librarian")
    public ResponseEntity<?> createLibrarian(
            @RequestParam String username, 
            @RequestParam String password,
            @RequestParam String fullName) {
        
        try {
            // Vérifier si l'utilisateur existe déjà
            if (userService.findByUsername(username).isPresent()) {
                return ResponseEntity.badRequest().body("Username already exists");
            }
            
            // Créer un utilisateur avec le rôle LIBRARIAN
            User librarian = User.builder()
                    .username(username)
                    .fullName(fullName)
                    .roles(Set.of(Role.ROLE_LIBRARIAN))
                    .build();
            
            // Utiliser le service pour hacher le mot de passe et sauvegarder
            userService.updatePassword(librarian, password);
            User saved = userService.save(librarian);
            
            return ResponseEntity.ok(saved);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error creating librarian: " + e.getMessage());
        }
    }

    /**
     * Lister tous les utilisateurs
     */
    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers() {
        try {
            List<User> users = userService.findAll();
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error fetching users: " + e.getMessage());
        }
    }

    /**
     * Promouvoir un utilisateur (changer son rôle)
     */
    @PostMapping("/promote")
    public ResponseEntity<?> promoteUser(
            @RequestParam String username,
            @RequestParam String newRole) {
        
        try {
            return userService.findByUsername(username)
                    .map(user -> {
                        Role role;
                        try {
                            // Convertir la string en enum Role
                            role = Role.valueOf(newRole);
                        } catch (IllegalArgumentException e) {
                            return ResponseEntity.badRequest()
                                    .body("Invalid role. Must be ROLE_ADMIN, ROLE_LIBRARIAN, or ROLE_MEMBER");
                        }
                        
                        // Mettre à jour les rôles
                        user.setRoles(Set.of(role));
                        User updated = userService.save(user);
                        
                        return ResponseEntity.ok(updated);
                    })
                    .orElse(ResponseEntity.status(404).body("User not found"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error promoting user: " + e.getMessage());
        }
    }

    /**
     * Supprimer un utilisateur (optionnel mais utile)
     */
    @DeleteMapping("/users/{username}")
    public ResponseEntity<?> deleteUser(@PathVariable String username) {
        try {
            return userService.findByUsername(username)
                    .map(user -> {
                        userService.delete(user.getId());
                        return ResponseEntity.ok("User deleted successfully");
                    })
                    .orElse(ResponseEntity.status(404).body("User not found"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error deleting user: " + e.getMessage());
        }
    }
}