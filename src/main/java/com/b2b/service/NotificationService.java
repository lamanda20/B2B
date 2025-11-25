package com.b2b.service;

import com.b2b.model.Notification;
import com.b2b.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository repo;

    public void notify(Long userId, String title, String message) {
        Notification n = new Notification();
        n.setUserId(userId);
        n.setTitle(title);
        n.setMessage(message);
        repo.save(n);
    }

    public List<Notification> getRecent(Long userId) {
        return repo.findTop5ByUserIdOrderByCreatedAtDesc(userId);
    }

    public List<Notification> getAll(Long userId) {
        return repo.findByUserIdOrderByCreatedAtDesc(userId);
    }

    public void markSeen(Long id) {
        repo.findById(id).ifPresent(n -> {
            n.setSeen(true);
            repo.save(n);
        });
    }
}
