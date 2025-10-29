package com.example.BiblioGestionAL.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.BiblioGestionAL.service.UserService;

@Configuration
public class AppConfig {

    @Bean
    public CommandLineRunner initAdmin(UserService userService) {
        return args -> {
            try {
                userService.createAdmin("admin", "admin123", "Super Admin");
                System.out.println("Admin créé : admin / admin123");
            } catch (IllegalArgumentException e) {
                // Ignore si déjà existant
            }
        };
    }
}