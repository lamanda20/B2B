package com.b2b.service;

import com.b2b.model.AdminUser;
import com.b2b.model.Company;
import com.b2b.repository.AdminUserRepository;
import com.b2b.repository.CompanyRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

    private final AdminUserRepository admins;
    private final CompanyRepository companies;
    private final PasswordEncoder encoder;

    public AccountService(AdminUserRepository admins,
                          CompanyRepository companies,
                          PasswordEncoder encoder) {
        this.admins = admins;
        this.companies = companies;
        this.encoder = encoder;
    }

    public void changePassword(String email, String currentRaw, String newRaw) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Utilisateur non authentifié");
        }
        email = email.toLowerCase();

        boolean found = false;

        // 1) Essayer AdminUser
        AdminUser admin = admins.findByEmail(email).orElse(null);
        if (admin != null) {
            found = true;

            if (!admin.isEnabled()) {
                throw new IllegalArgumentException("Compte administrateur désactivé");
            }

            if (!encoder.matches(currentRaw, admin.getPassword())) {
                throw new IllegalArgumentException("Mot de passe actuel invalide");
            }

            if (encoder.matches(newRaw, admin.getPassword())) {
                throw new IllegalArgumentException("Le nouveau mot de passe doit être différent de l'ancien");
            }

            admin.setPassword(encoder.encode(newRaw));
            admins.save(admin);
        }

        // 2) Sinon Company
        if (!found) {
            Company company = companies.findByEmail(email).orElse(null);
            if (company == null) {
                throw new IllegalArgumentException("Compte introuvable");
            }

            if (!company.isEnabled()) {
                throw new IllegalArgumentException("Compte entreprise désactivé");
            }

            if (!encoder.matches(currentRaw, company.getPassword())) {
                throw new IllegalArgumentException("Mot de passe actuel invalide");
            }

            if (encoder.matches(newRaw, company.getPassword())) {
                throw new IllegalArgumentException("Le nouveau mot de passe doit être différent de l'ancien");
            }

            company.setPassword(encoder.encode(newRaw));
            companies.save(company);
        }
    }
}
