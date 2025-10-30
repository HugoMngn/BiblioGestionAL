package com.example.BiblioGestionAL.dto;

import lombok.*;

public class AuthDTOs {
    // Registration DTOs
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class RegisterRequest {
        private String username;
        private String password;
        private String fullName;
    }

    // Login DTOs
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class LoginRequest {
        private String username;
        private String password;
    }

    // Login Response DTO
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class LoginResponse {
        private String username;
        private String message;
        private String role;
    }

    // Profile Update DTO
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProfileUpdateRequest {
        private String username;
        private String fullName;
    }

    // Password Change DTO
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PasswordChangeRequest {
        private String username;
        private String oldPassword;
        private String newPassword;
    }

    // Change Password DTO
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChangePasswordRequest {
        private String username;
        private String oldPassword;
        private String newPassword;
    }
}