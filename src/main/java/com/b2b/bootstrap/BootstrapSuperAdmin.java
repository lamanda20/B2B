package com.b2b.bootstrap;

import com.b2b.model.Company;
import com.b2b.model.Role;
import com.b2b.repository.CompanyRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@Profile({"dev","test","local"})
@Order(1)
public class BootstrapSuperAdmin implements CommandLineRunner {

    private final CompanyRepository users;
    private final PasswordEncoder encoder;

    public BootstrapSuperAdmin(CompanyRepository users, PasswordEncoder encoder) {
        this.users = users;
        this.encoder = encoder;
    }

    @Override
    public void run(String... args) {
        if (users.countByRole(Role.SUPER_ADMIN) > 0) {
            System.out.println("✅ SuperAdmin already exists, password not changed");
            return;
        }

        Company sa = new Company();
        sa.setMustChangePassword(true);
        sa.setFullName("Super Admin"); // Make sure this setter exists in Company
        sa.setEmail("superadmin@b2b.local");
        sa.setPassword(encoder.encode(defaultPassword()));
        sa.setRole(Role.SUPER_ADMIN); // Make sure this setter exists in Company
        // If setCompany does not exist, remove or adjust this line
        // sa.setCompany(null);
        sa.setEnabled(true); // Make sure this setter exists in Company
        users.save(sa);

        System.out.println("""
                ==========================================
                SUPER_ADMIN created:
                  email    : superadmin@b2b.local
                  password : """ + defaultPassword() + """
                (change it later via the /api/account/password endpoint)
                ==========================================
                """);
    }

    private String defaultPassword() {
        return "Admin@2025";
    }
}