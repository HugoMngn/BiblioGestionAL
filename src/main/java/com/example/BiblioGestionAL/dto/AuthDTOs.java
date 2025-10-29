package com.example.BiblioGestionAL.dto;

import java.util.Set;

import com.example.BiblioGestionAL.entity.Role;
import com.example.BiblioGestionAL.entity.User;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class AuthDTOs {

    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    public static class RegisterRequest {
        @NotBlank(message = "Le nom d'utilisateur est requis")
        @Size(min = 3, max = 20)
        private String username;

        @NotBlank(message = "Le mot de passe est requis")
        @Size(min = 6)
        private String password;

        @NotBlank(message = "Le nom complet est requis")
        private String fullName;
    }

    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    public static class LoginRequest {
        @NotBlank private String username;
        @NotBlank private String password;
    }

    @Data @Builder
    public static class LoginResponse {
        private String username;
        private String fullName;
        private String message;
    }

    @Data @Builder
    public static class RegisterResponse {
        private String username;
        private String fullName;
        private Set<Role> roles;
        private String message;

        public static RegisterResponse fromUser(User user) {
            return RegisterResponse.builder()
                    .username(user.getUsername())
                    .fullName(user.getFullName())
                    .roles(user.getRoles())
                    .message("Inscription réussie")
                    .build();
        }
    }

    @Data @Builder
    public static class ProfileResponse {
        private String username;
        private String fullName;
        private Set<Role> roles;

        public static ProfileResponse fromUser(User user) {
            return ProfileResponse.builder()
                    .username(user.getUsername())
                    .fullName(user.getFullName())
                    .roles(user.getRoles())
                    .build();
        }
    }

    @Data @NoArgsConstructor @AllArgsConstructor
    public static class UpdateProfileRequest {
        private String fullName;

        @Size(min = 6, message = "Le nouveau mot de passe doit faire au moins 6 caractères")
        private String newPassword;
    }
}