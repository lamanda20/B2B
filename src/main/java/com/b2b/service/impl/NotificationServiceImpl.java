package com.b2b.service.impl;

import com.b2b.model.Company;
import com.b2b.model.Notification;
import com.b2b.repository.CompanyRepository;
import com.b2b.repository.NotificationRepository;
import com.b2b.service.NotificationService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final CompanyRepository companyRepository;

    public NotificationServiceImpl(NotificationRepository notificationRepository,
                                   CompanyRepository companyRepository) {
        this.notificationRepository = notificationRepository;
        this.companyRepository = companyRepository;
    }

    @Override
    public void sendToCompany(Long companyId, String message) {

        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new RuntimeException("Company not found: " + companyId));

        Notification notif = new Notification();
        notif.setCompany(company);
        notif.setMessage(message);
        notif.setCreatedAt(LocalDateTime.now());
        notif.setRead(false);

        notificationRepository.save(notif);
    }

    @Override
    public List<Notification> getForCompany(Long companyId) {
        return notificationRepository.findByCompanyIdOrderByCreatedAtDesc(companyId);
    }

    @Override
    public void markAsRead(Long id) {
        Notification n = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found"));

        n.setRead(true);
        notificationRepository.save(n);
    }
}
