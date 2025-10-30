package com.example.BiblioGestionAL.service;

import java.util.Optional;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.BiblioGestionAL.entity.Role;
import com.example.BiblioGestionAL.entity.User;
import com.example.BiblioGestionAL.factory.UserFactory;
import com.example.BiblioGestionAL.repository.UserRepository;

// Service class for managing User entities.
@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserFactory userFactory;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // Constructor with Dependency Injection
    @Autowired
    public UserService(UserRepository userRepository, UserFactory userFactory) {
        this.userRepository = userRepository;
        this.userFactory = userFactory;
    }

    // Register a new member user
    public User registerMember(String username, String rawPassword, String fullName) {
        if (userRepository.findByUsername(username).isPresent())
            throw new IllegalArgumentException("Username already exists");
        String hashed = passwordEncoder.encode(rawPassword);
        User user = userFactory.createUser(username, hashed, fullName, Role.ROLE_MEMBER);
        return userRepository.save(user);
    }

    // Find user by username
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    // Save or update user
    public User save(User user) {
        return userRepository.save(user);
    }

    // Check if raw password matches stored password
    public boolean checkPassword(User user, String rawPassword) {
        return passwordEncoder.matches(rawPassword, user.getPassword());
    }

    // Create an admin user
    public User createAdmin(String username, String rawPassword, String fullName) {
        String hashed = passwordEncoder.encode(rawPassword);
        User admin = userFactory.createUser(username, hashed, fullName, Role.ROLE_ADMIN);
        return userRepository.save(admin);
    }

    // Encode a raw password
    public String encodePassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    // Update user's password
    public void updatePassword(User user, String newPassword) {
        user.setPassword(encodePassword(newPassword));
        userRepository.save(user);
    }

    // Find all users
    public List<User> findAll() {
        return userRepository.findAll();
    }

    // Delete user by ID
    public void delete(Long id) {
        userRepository.deleteById(id);
    }
    
}
