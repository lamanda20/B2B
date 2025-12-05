// src/main/java/com/b2b/controller/NotificationController.java
package com.b2b.controller;

import com.b2b.model.Notification;
import com.b2b.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping("/{companyId}")
    public List<Notification> getNotifications(@PathVariable Long companyId) {
        return notificationService.getForCompany(companyId);
    }

    @PostMapping("/{id}/read")
    public void markAsRead(@PathVariable Long id) {
        notificationService.markAsRead(id);
    }
}
