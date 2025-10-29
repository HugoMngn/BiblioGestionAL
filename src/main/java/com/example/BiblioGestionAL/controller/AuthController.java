package com.example.BiblioGestionAL.controller;

import com.example.BiblioGestionAL.dto.AuthDTOs;
import com.example.BiblioGestionAL.entity.User;
import com.example.BiblioGestionAL.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Minimal auth controller (no JWT for simplicity). In real projet, utiliser JWT or sessions.
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserService userService;
    @Autowired
    public AuthController(UserService userService) { this.userService = userService; }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody AuthDTOs.RegisterRequest req) {
        User u = userService.registerMember(req.getUsername(), req.getPassword(), req.getFullName());
        return ResponseEntity.ok(u);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthDTOs.LoginRequest req) {
        return userService.findByUsername(req.getUsername())
                .map(u -> {
                    if (userService.checkPassword(u, req.getPassword())) {
                        AuthDTOs.LoginResponse resp = AuthDTOs.LoginResponse.builder()
                                .username(u.getUsername())
                                .message("Login success")
                                .build();
                        return ResponseEntity.ok(resp);
                    } else {
                        return ResponseEntity.status(401).body("Invalid credentials");
                    }
                }).orElse(ResponseEntity.status(404).body("User not found"));
    }
}
