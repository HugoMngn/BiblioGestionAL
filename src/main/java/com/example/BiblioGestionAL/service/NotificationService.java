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

/**
 * Notifications are not implemented using Observer. Instead, we store notifications and optionally send mails.
 */
@Service
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

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

    // Ex: scheduled task to generate reminders for due loans (mocked)
    // Runs every day at 8am
    @Scheduled(cron = "0 0 8 * * ?")
    public void dailyReminderJob() {
        // In a real app: query loans nearing due date -> create notifications.
        // Here we leave it simple due to scope.
    }
}
