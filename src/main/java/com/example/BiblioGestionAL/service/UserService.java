package com.example.BiblioGestionAL.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.BiblioGestionAL.entity.Role;
import com.example.BiblioGestionAL.entity.User;
import com.example.BiblioGestionAL.factory.UserFactory;
import com.example.BiblioGestionAL.repository.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserFactory userFactory;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Autowired
    public UserService(UserRepository userRepository, UserFactory userFactory) {
        this.userRepository = userRepository;
        this.userFactory = userFactory;
    }

    public User registerMember(String username, String rawPassword, String fullName) {
        if (userRepository.findByUsername(username).isPresent())
            throw new IllegalArgumentException("Username already exists");
        String hashed = passwordEncoder.encode(rawPassword);
        User user = userFactory.createUser(username, hashed, fullName, Role.ROLE_MEMBER);
        return userRepository.save(user);
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

    // Admin creation method
    public User createAdmin(String username, String rawPassword, String fullName) {
        String hashed = passwordEncoder.encode(rawPassword);
        User admin = userFactory.createUser(username, hashed, fullName, Role.ROLE_ADMIN);
        return userRepository.save(admin);
    }
}
