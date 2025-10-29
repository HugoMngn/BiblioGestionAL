package com.example.BiblioGestionAL.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.BiblioGestionAL.entity.User;
import com.example.BiblioGestionAL.factory.UserFactory;
import com.example.BiblioGestionAL.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserFactory userFactory;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, UserFactory userFactory, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userFactory = userFactory;
        this.passwordEncoder = passwordEncoder;
    }

    // INSCRIPTION MEMBRE
    public User registerMember(String username, String password, String fullName) {
        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("Ce nom d'utilisateur existe déjà");
        }
        User user = userFactory.createMember(username, passwordEncoder.encode(password), fullName);
        return userRepository.save(user);
    }

    // CRÉATION BIBLIOTHÉCAIRE
    public User createLibrarian(String username, String password, String fullName) {
        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("Ce nom d'utilisateur existe déjà");
        }
        User librarian = userFactory.createLibrarian(username, passwordEncoder.encode(password), fullName);
        return userRepository.save(librarian);
    }

    // CRÉATION ADMIN
    public User createAdmin(String username, String password, String fullName) {
        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("Ce nom d'utilisateur existe déjà");
        }
        User admin = userFactory.createAdmin(username, passwordEncoder.encode(password), fullName);
        return userRepository.save(admin);
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public boolean checkPassword(User user, String rawPassword) {
        return passwordEncoder.matches(rawPassword, user.getPassword());
    }

    public User updateProfile(String username, String fullName, String newPassword) {
        User user = findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur non trouvé"));

        if (fullName != null && !fullName.isBlank()) {
            user.setFullName(fullName);
        }
        if (newPassword != null && !newPassword.isBlank()) {
            user.setPassword(passwordEncoder.encode(newPassword));
        }
        return userRepository.save(user);
    }
}