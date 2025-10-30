package com.example.BiblioGestionAL.config;

import com.example.BiblioGestionAL.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/* Security Configuration  */
@Configuration
public class SecurityConfig {

    private final UserRepository userRepository;

    // Constructor
    @Autowired
    public SecurityConfig(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // UserDetailsService bean
    @Bean
    public UserDetailsService userDetailsService() {
        return username -> userRepository.findByUsername(username)
                .map(u -> {
                    // map roles to authorities
                    return User.withUsername(u.getUsername())
                            .password(u.getPassword())
                            .authorities(u.getRoles().stream().map(Enum::name).toArray(String[]::new))
                            .build();
                }).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    // Password encoder bean
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Security filter chain bean
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/**").permitAll()
                .anyRequest().permitAll() // pour faciliter tests ; en prod lock down
            )
            .httpBasic(Customizer.withDefaults());
        return http.build();
    }
}
