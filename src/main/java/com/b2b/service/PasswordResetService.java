package com.b2b.service;

import com.b2b.model.AdminUser;
import com.b2b.model.Company;
import com.b2b.model.PasswordResetToken;
import com.b2b.repository.AdminUserRepository;
import com.b2b.repository.CompanyRepository;
import com.b2b.repository.PasswordResetTokenRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class PasswordResetService {

    private final AdminUserRepository admins;
    private final CompanyRepository companies;
    private final PasswordResetTokenRepository tokens;
    private final PasswordEncoder encoder;

    // durée de validité du token (en minutes)
    private static final long EXPIRATION_MINUTES = 30;

    public PasswordResetService(AdminUserRepository admins,
                                CompanyRepository companies,
                                PasswordResetTokenRepository tokens,
                                PasswordEncoder encoder) {
        this.admins = admins;
        this.companies = companies;
        this.tokens = tokens;
        this.encoder = encoder;
    }

    /**
     * Crée un token de reset pour l'email s'il existe (admin ou company).
     * Retourne le token pour logs / email.
     */
    public Optional<String> createResetTokenForEmail(String email) {
        if (email == null || email.isBlank()) {
            return Optional.empty();
        }
        email = email.toLowerCase();

        boolean exists = admins.findByEmail(email).isPresent()
                || companies.existsByEmail(email);

        if (!exists) {
            // On ne révèle pas si l'email existe ou non
            return Optional.empty();
        }

        // Créer le token
        String rawToken = UUID.randomUUID().toString();

        PasswordResetToken prt = new PasswordResetToken();
        prt.setEmail(email);
        prt.setToken(rawToken);
        prt.setExpiresAt(LocalDateTime.now().plusMinutes(EXPIRATION_MINUTES));
        prt.setUsed(false);

        tokens.save(prt);

        return Optional.of(rawToken);
    }

    /**
     * Applique un reset de mot de passe à partir d'un token et d'un nouveau mot de passe.
     */
    public void resetPassword(String token, String newRawPassword) {
        if (token == null || token.isBlank()) {
            throw new IllegalArgumentException("Token invalide");
        }

        PasswordResetToken prt = tokens.findByToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Token invalide"));

        if (prt.isUsed()) {
            throw new IllegalArgumentException("Ce lien de réinitialisation a déjà été utilisé");
        }

        if (prt.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Ce lien de réinitialisation a expiré");
        }

        String email = prt.getEmail().toLowerCase();

        boolean found = false;

        // Tentative Admin
        AdminUser admin = admins.findByEmail(email).orElse(null);
        if (admin != null) {
            found = true;
            if (!admin.isEnabled()) {
                throw new IllegalArgumentException("Compte administrateur désactivé");
            }
            admin.setPassword(encoder.encode(newRawPassword));
            admins.save(admin);
        }

        // Sinon Company
        if (!found) {
            Company company = companies.findByEmail(email).orElse(null);
            if (company == null) {
                throw new IllegalArgumentException("Compte introuvable");
            }
            if (!company.isEnabled()) {
                throw new IllegalArgumentException("Compte entreprise désactivé");
            }
            company.setPassword(encoder.encode(newRawPassword));
            companies.save(company);
        }

        prt.setUsed(true);
        tokens.save(prt);
    }
}
