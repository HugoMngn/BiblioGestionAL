package com.example.BiblioGestionAL.factory;

import java.util.Set;

import org.springframework.stereotype.Component;

import com.example.BiblioGestionAL.entity.Role;
import com.example.BiblioGestionAL.entity.User;

@Component
public class UserFactory {

    public User createMember(String username, String encodedPassword, String fullName) {
        return createUser(username, encodedPassword, fullName, Role.ROLE_MEMBER);
    }

    public User createLibrarian(String username, String encodedPassword, String fullName) {
        return createUser(username, encodedPassword, fullName, Role.ROLE_LIBRARIAN);
    }

    public User createAdmin(String username, String encodedPassword, String fullName) {
        return createUser(username, encodedPassword, fullName, Role.ROLE_ADMIN);
    }

    private User createUser(String username, String encodedPassword, String fullName, Role role) {
        return User.builder()
                .username(username)
                .password(encodedPassword)
                .fullName(fullName)
                .roles(Set.of(role))
                .build();
    }
}