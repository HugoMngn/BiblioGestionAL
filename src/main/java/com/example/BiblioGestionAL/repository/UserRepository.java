package com.example.BiblioGestionAL.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.BiblioGestionAL.entity.User;

// Repository interface for User entity with custom query methods.
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
