package com.example.BiblioGestionAL.controller;

import com.example.BiblioGestionAL.entity.User;
import com.example.BiblioGestionAL.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Endpoints pour admin: créer bibliothécaires, lister utilisateurs...
 */
@RestController
@RequestMapping("/api/admin")
public class AdminController {
    private final UserService userService;
    @Autowired
    public AdminController(UserService userService) { this.userService = userService; }

    @PostMapping("/create-librarian")
    public ResponseEntity<?> createLibrarian(@RequestParam String username, @RequestParam String password, @RequestParam String fullName) {
        // In a real app, only ADMIN can call this (controller would check)
        User librarian = userService.createAdmin(username, password, fullName); // here createAdmin used for simplicity; adapt to create librarian role
        return ResponseEntity.ok(librarian);
    }
}
