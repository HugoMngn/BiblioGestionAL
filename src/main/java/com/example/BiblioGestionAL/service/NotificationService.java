package com.example.BiblioGestionAL.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.example.BiblioGestionAL.entity.Notification;
import com.example.BiblioGestionAL.entity.Role;
import com.example.BiblioGestionAL.entity.User;
import com.example.BiblioGestionAL.repository.NotificationRepository;
import com.example.BiblioGestionAL.repository.UserRepository;

// Service class for managing Notification entities.
@Service
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    // Constructor with Dependency Injection
    @Autowired
    public NotificationService(NotificationRepository notificationRepository, UserRepository userRepository) {
        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
    }

    public Notification notifyUser(User user, String message) {
        Notification n = Notification.builder()
                .recipient(user)
                .message(message)
                .build();
        return notificationRepository.save(n);
    }

    public void notifyLibrarians(String message) {
        List<User> users = userRepository.findAll();
        for (User u : users) {
            if (u.getRoles().contains(Role.ROLE_LIBRARIAN) || u.getRoles().contains(Role.ROLE_ADMIN)) {
                notifyUser(u, message);
            }
        }
    }

    public List<Notification> getNotificationsFor(User user) {
        return notificationRepository.findByRecipient(user);
    }

    @Scheduled(cron = "0 0 8 * * ?")
    public void dailyReminderJob() {
    }
}
