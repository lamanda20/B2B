package com.b2b.bootstrap;

import com.b2b.model.AppUser;
import com.b2b.model.Role;
import com.b2b.repository.AppUserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@Profile({"dev","test"}) // création uniquement en dev/test
public class BootstrapSuperAdmin implements CommandLineRunner {

    private final AppUserRepository users;
    private final PasswordEncoder encoder;

    public BootstrapSuperAdmin(AppUserRepository users, PasswordEncoder encoder) {
        this.users = users;
        this.encoder = encoder;
    }

    @Override
    public void run(String... args) {
        // Si un superadmin existe déjà, ne rien faire
        if (users.countByRole(Role.SUPER_ADMIN) > 0) {
            System.out.println("✅ SuperAdmin déjà existant, mot de passe non modifié");
            return;
        }

        // Sinon, créer un nouveau superadmin avec mot de passe initial
        AppUser sa = new AppUser();
        sa.setMustChangePassword(true);
        sa.setFullName("Super Admin");
        sa.setEmail("superadmin@b2b.local");
        sa.setPassword(encoder.encode(defaultPassword())); // mot de passe initial encodé
        sa.setRole(Role.SUPER_ADMIN);
        sa.setCompany(null);
        sa.setEnabled(true);
        users.save(sa);

        System.out.println("""
                ==========================================
                SUPER_ADMIN créé :
                  email    : superadmin@b2b.local
                  password : """ + defaultPassword() + """
                (change-le ensuite via l'endpoint /api/account/password)
                ==========================================
                """);
    }

    private String defaultPassword() {
        String env = System.getenv("B2B_SUPERADMIN_PASSWORD");
        return (env != null && !env.isBlank()) ? env : "ChangeMe!123";
    }
}
