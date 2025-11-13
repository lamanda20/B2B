package com.b2b.service;

import com.b2b.model.AdminUser;
import com.b2b.model.Company;
import com.b2b.repository.AdminUserRepository;
import com.b2b.repository.CompanyRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

    private final CompanyRepository companies;
    private final AdminUserRepository admins;
    private final PasswordEncoder encoder;

    public AccountService(CompanyRepository companies,
                          AdminUserRepository admins,
                          PasswordEncoder encoder) {
        this.companies = companies;
        this.admins = admins;
        this.encoder = encoder;
    }

    public void changePassword(String email, String currentPassword, String newPassword) {
        String normalized = email.toLowerCase();

        // 1) Essayer en tant qu'ADMIN
        var adminOpt = admins.findByEmail(normalized);
        if (adminOpt.isPresent()) {
            AdminUser admin = adminOpt.get();

            if (!admin.isEnabled())
                throw new IllegalArgumentException("Compte admin désactivé");
            if (!encoder.matches(currentPassword, admin.getPassword()))
                throw new IllegalArgumentException("Mot de passe actuel invalide");
            if (encoder.matches(newPassword, admin.getPassword()))
                throw new IllegalArgumentException("Le nouveau mot de passe doit être différent");

            admin.setPassword(encoder.encode(newPassword));
            admins.save(admin);
            return;
        }

        // 2) Sinon, ENTREPRISE (Company)
        Company company = companies.findByEmail(normalized)
                .orElseThrow(() -> new IllegalArgumentException("Compte introuvable"));
        if (!company.isEnabled())
            throw new IllegalArgumentException("Compte désactivé");
        if (!encoder.matches(currentPassword, company.getPassword()))
            throw new IllegalArgumentException("Mot de passe actuel invalide");
        if (encoder.matches(newPassword, company.getPassword()))
            throw new IllegalArgumentException("Le nouveau mot de passe doit être différent");

        company.setPassword(encoder.encode(newPassword));
        companies.save(company);
    }
}
