package com.example.BiblioGestionAL.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.BiblioGestionAL.entity.Notification;
import com.example.BiblioGestionAL.entity.User;

// Repository interface for Notification entity with custom query methods.
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByRecipient(User recipient);
}
