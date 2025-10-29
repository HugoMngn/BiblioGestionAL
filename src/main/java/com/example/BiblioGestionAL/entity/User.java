package com.example.BiblioGestionAL.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "usr") // évite collision avec mot clé user
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 191)
    private String username;

    @Column(nullable = false)
    private String password; // stocker hashé en production

    private String fullName;

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role")
    @Builder.Default
    private Set<Role> roles = new HashSet<>();
    
        /**
     * Retourne le rôle le plus élevé de l'utilisateur
     * Hiérarchie : ADMIN > LIBRARIAN > MEMBER
     */
    public String getPrimaryRole() {
        if (roles.contains(Role.ROLE_ADMIN)) {
            return "ROLE_ADMIN";
        }
        if (roles.contains(Role.ROLE_LIBRARIAN)) {
            return "ROLE_LIBRARIAN";
        }
        if (roles.contains(Role.ROLE_MEMBER)) {
            return "ROLE_MEMBER";
        }
        return "ROLE_MEMBER"; // par défaut
    }
}

