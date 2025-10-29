package com.example.BiblioGestionAL.controller;

import com.example.BiblioGestionAL.dto.AuthDTOs;
import com.example.BiblioGestionAL.entity.User;
import com.example.BiblioGestionAL.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
/**
 * Endpoints pour authentification et gestion utilisateurs
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserService userService;
    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    /// Register member
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody AuthDTOs.RegisterRequest req) {
        User u = userService.registerMember(req.getUsername(), req.getPassword(), req.getFullName());
        return ResponseEntity.ok(u);
    }

    /// Login
    @PostMapping("/login")
public ResponseEntity<?> login(@RequestBody AuthDTOs.LoginRequest req) {
    return userService.findByUsername(req.getUsername())
            .map(u -> {
                if (userService.checkPassword(u, req.getPassword())) {
                    AuthDTOs.LoginResponse resp = AuthDTOs.LoginResponse.builder()
                            .username(u.getUsername())
                            .message("Login success")
                            .role(u.getPrimaryRole())
                            .build();
                    return ResponseEntity.ok(resp);
                } else {
                    return ResponseEntity.status(401).body("Invalid credentials");
                }
            }).orElse(ResponseEntity.status(404).body("User not found"));
}

    /// Change password
    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody AuthDTOs.ChangePasswordRequest req) {
        return userService.findByUsername(req.getUsername())
                .map(u -> {
                    if (userService.checkPassword(u, req.getOldPassword())) {
                        userService.updatePassword(u, req.getNewPassword());
                        return ResponseEntity.ok("Password updated");
                    } else {
                        return ResponseEntity.status(401).body("Invalid current password");
                    }
                }).orElse(ResponseEntity.status(404).body("User not found"));
    }

    /// Get user info
    @GetMapping("/user/{username}")
    public ResponseEntity<?> getUserInfo(@PathVariable String username) {
        return userService.findByUsername(username)
                .map(u -> ResponseEntity.ok("User found: " + u))
                .orElse(ResponseEntity.status(404).body("User not found"));
    }


    /// Create admin user
    @PostMapping("/admin/create")
    public ResponseEntity<?> createAdmin(@RequestBody AuthDTOs.RegisterRequest req) {
        User u = userService.createAdmin(req.getUsername(), req.getPassword(), req.getFullName());
        return ResponseEntity.ok(u);
    }

    /// Update user profile
    @PutMapping("/user/update")
    public ResponseEntity<?> updateProfile(@RequestBody AuthDTOs.ProfileUpdateRequest req) {
        return userService.findByUsername(req.getUsername())
                .map(u -> {
                    u.setFullName(req.getFullName());
                    User updated = userService.save(u);
                    return ResponseEntity.ok("User updated: " + updated);
                }).orElse(ResponseEntity.status(404).body("User not found"));
    }
}
