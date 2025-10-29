package com.example.BiblioGestionAL.factory;

import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.example.BiblioGestionAL.entity.Role;
import com.example.BiblioGestionAL.entity.User;

/**
 * Factory Method pattern for creating users with default roles depending on type.
 */
@Component
public class UserFactory {

    public User createUser(String username, String password, String fullName, Role role) {
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        return User.builder()
                .username(username)
                .password(password)
                .fullName(fullName)
                .roles(roles)
                .build();
    }
}
