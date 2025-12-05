package com.b2b.service;

import com.b2b.model.Notification;

import java.util.List;

public interface NotificationService {

    void sendToCompany(Long companyId, String message);

    List<Notification> getForCompany(Long companyId);

    void markAsRead(Long id);
}
