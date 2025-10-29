package com.example.BiblioGestionAL.controller;

import java.security.Principal;
import java.util.Optional;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.BiblioGestionAL.dto.AuthDTOs;
import com.example.BiblioGestionAL.entity.User;
import com.example.BiblioGestionAL.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody AuthDTOs.RegisterRequest req) {
        try {
            User user = userService.registerMember(req.getUsername(), req.getPassword(), req.getFullName());
            return ResponseEntity.ok(AuthDTOs.RegisterResponse.fromUser(user));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody AuthDTOs.LoginRequest req) {
        return toUserResponse(
                userService.findByUsername(req.getUsername()),
                user -> {
                    if (userService.checkPassword(user, req.getPassword())) {
                        return ResponseEntity.ok(AuthDTOs.LoginResponse.builder()
                                .username(user.getUsername())
                                .fullName(user.getFullName())
                                .message("Connexion réussie")
                                .build());
                    } else {
                        return ResponseEntity.status(401).body("Mot de passe incorrect");
                    }
                }
        );
    }

    @GetMapping("/me")
    public ResponseEntity<?> getProfile(Principal principal) {
        return toUserResponse(
                userService.findByUsername(principal.getName()),
                user -> ResponseEntity.ok(AuthDTOs.ProfileResponse.fromUser(user))
        );
    }

    @PutMapping("/me")
    public ResponseEntity<?> updateProfile(Principal principal, @Valid @RequestBody AuthDTOs.UpdateProfileRequest req) {
        String username = principal.getName();
        return toUserResponse(
                userService.findByUsername(username),
                user -> {
                    try {
                        User updated = userService.updateProfile(username, req.getFullName(), req.getNewPassword());
                        return ResponseEntity.ok(AuthDTOs.ProfileResponse.fromUser(updated));
                    } catch (IllegalArgumentException e) {
                        return ResponseEntity.badRequest().body(e.getMessage());
                    }
                }
        );
    }

    @PostMapping("/librarian")
    public ResponseEntity<?> createLibrarian(@Valid @RequestBody AuthDTOs.RegisterRequest req) {
        try {
            User librarian = userService.createLibrarian(req.getUsername(), req.getPassword(), req.getFullName());
            return ResponseEntity.ok(AuthDTOs.RegisterResponse.fromUser(librarian));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/admin")
    public ResponseEntity<?> createAdmin(@Valid @RequestBody AuthDTOs.RegisterRequest req) {
        try {
            User admin = userService.createAdmin(req.getUsername(), req.getPassword(), req.getFullName());
            return ResponseEntity.ok(AuthDTOs.RegisterResponse.fromUser(admin));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    private ResponseEntity<?> toUserResponse(Optional<User> userOpt, Function<User, ResponseEntity<?>> mapper) {
        return userOpt
                .map(mapper)
                .orElseGet(() -> ResponseEntity.status(404).body("Utilisateur non trouvé"));
    }
}