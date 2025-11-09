package com.b2b.bootstrap;

import com.b2b.model.AppUser;
import com.b2b.repository.AppUserRepository;
import com.b2b.service.CustomUserDetailsService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class SuperAdminTestRunner implements CommandLineRunner {

    private final AppUserRepository userRepository;
    private final CustomUserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    public SuperAdminTestRunner(AppUserRepository userRepository,
                                CustomUserDetailsService userDetailsService,
                                PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        String email = "superadmin@b2b.local";
        String rawPassword = "ChangeMe!123";

        // Vérifie que l'utilisateur existe en DB
        AppUser user = userRepository.findByEmailIgnoreCase(email)
                .orElse(null);
        if (user == null) {
            System.out.println("❌ SuperAdmin non trouvé en base !");
            return;
        }

        System.out.println("✅ SuperAdmin trouvé en base : " + user.getEmail());
        System.out.println("Mot de passe hashé en DB : " + user.getPassword());

        // Vérifie que PasswordEncoder correspond
        boolean matches = passwordEncoder.matches(rawPassword, user.getPassword());
        System.out.println("Password matches: " + matches);

        // Vérifie que UserDetailsService fonctionne
        try {
            var userDetails = userDetailsService.loadUserByUsername(email);
            System.out.println("UserDetailsService OK : " + userDetails.getUsername());
        } catch (Exception e) {
            System.out.println("❌ UserDetailsService erreur : " + e.getMessage());
        }
    }
}
