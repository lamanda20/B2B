package com.b2b.service;

import com.b2b.model.AppUser;
import com.b2b.repository.AppUserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AccountService {
    private final AppUserRepository users; private final PasswordEncoder encoder;
    public AccountService(AppUserRepository users, PasswordEncoder encoder) {
        this.users = users; this.encoder = encoder;
    }

    public void changePassword(String email, String currentRaw, String newRaw) {
        AppUser u = users.findByEmailIgnoreCase(email).orElseThrow();

        if (!encoder.matches(currentRaw, u.getPassword()))
            throw new IllegalArgumentException("Mot de passe actuel invalide");

        if (encoder.matches(newRaw, u.getPassword()))
            throw new IllegalArgumentException("Le nouveau mot de passe doit être différent");

        // (Option) vérif complexité ici
        u.setPassword(encoder.encode(newRaw));
        u.setMustChangePassword(false);
        users.save(u);
    }
}
