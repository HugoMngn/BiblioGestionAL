package com.example.BiblioGestionAL.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "notification")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Notification {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Recipient of the notification
    @ManyToOne
    private User recipient;

    private String message;

    // Notification status
    @Builder.Default
    private boolean readFlag = false;
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
}

