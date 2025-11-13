package com.b2b.controller;

import com.b2b.auth.LoginResponse;
import com.b2b.auth.UserPayload;
import com.b2b.security.JwtUtil;
import com.b2b.model.AdminUser;
import com.b2b.model.Company;
import com.b2b.repository.AdminUserRepository;
import com.b2b.repository.CompanyRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AdminUserRepository admins;
    private final CompanyRepository companies;
    private final PasswordEncoder encoder;
    private final JwtUtil jwt;

    public AuthController(AdminUserRepository admins,
                          CompanyRepository companies,
                          PasswordEncoder encoder,
                          JwtUtil jwt) {
        this.admins = admins;
        this.companies = companies;
        this.encoder = encoder;
        this.jwt = jwt;
    }

    // DTO d'entrée (équivalent à ton AuthRequest interne)
    public static class LoginRequest {
        private String email;
        private String password;

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        String email = (request.getEmail() == null) ? "" : request.getEmail().toLowerCase();
        String raw = (request.getPassword() == null) ? "" : request.getPassword();

        try {
            // 1) Tentative ADMIN
            var adminOpt = admins.findByEmail(email);
            if (adminOpt.isPresent()) {
                AdminUser a = adminOpt.get();
                if (!a.isEnabled())
                    return ResponseEntity.status(401).body(Map.of("error","USER_DISABLED"));

                if (!encoder.matches(raw, a.getPassword()))
                    return ResponseEntity.status(401).body(Map.of("error","BAD_CREDENTIALS"));

                // Générer JWT avec rôle ADMIN (companyId = null)
                String token = jwt.generateToken(null, email, "ADMIN", null);
                var payload = new UserPayload(null, email, "ADMIN", null);
                // mustChangePassword: si tu ne gères plus ce champ, renvoie false
                return ResponseEntity.ok(new LoginResponse(token, payload, false));
            }

            // 2) Tentative COMPANY
            Company c = companies.findByEmail(email)
                    .orElse(null);
            if (c == null)
                return ResponseEntity.status(401).body(Map.of("error","BAD_CREDENTIALS"));

            if (!c.isEnabled())
                return ResponseEntity.status(401).body(Map.of("error","USER_DISABLED"));

            if (!encoder.matches(raw, c.getPassword()))
                return ResponseEntity.status(401).body(Map.of("error","BAD_CREDENTIALS"));

            // Générer JWT avec rôle COMPANY (companyId = c.getId())
            String token = jwt.generateToken(
                    c.getId(),                 // userId (tu peux mettre null si tu ne l’utilises pas)
                    email,
                    "COMPANY",
                    c.getId()                  // companyId dans tes claims
            );
            var payload = new UserPayload(
                    c.getId(),                 // userId dans ton payload (ou null)
                    email,
                    "COMPANY",
                    c.getId()
            );
            return ResponseEntity.ok(new LoginResponse(token, payload, false)); // pas de mustChangePassword côté Company pour l’instant

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of("error","SERVER_ERROR","detail",e.getClass().getSimpleName()));
        }
    }
}
