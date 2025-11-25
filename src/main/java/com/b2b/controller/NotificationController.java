package com.b2b.controller;

import com.b2b.model.Notification;
import com.b2b.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    private NotificationService service;

    @GetMapping("/recent/{userId}")
    public List<Notification> recent(@PathVariable Long userId) {
        return service.getRecent(userId);
    }

    @GetMapping("/all/{userId}")
    public List<Notification> all(@PathVariable Long userId) {
        return service.getAll(userId);
    }

    @PutMapping("/seen/{id}")
    public void seen(@PathVariable Long id) {
        service.markSeen(id);
    }
}
