package com.b2b.controller;

import com.b2b.auth.LoginResponse;
import com.b2b.auth.UserPayload;
import com.b2b.security.JwtUtil;
import com.b2b.dto.LoginRequest;
import com.b2b.dto.RegisterRequest;
import com.b2b.dto.ApiResponse;
import com.b2b.model.AdminUser;
import com.b2b.model.Company;
import com.b2b.repository.AdminUserRepository;
import com.b2b.repository.CompanyRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import com.b2b.service.PasswordResetService;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AdminUserRepository admins;
    private final CompanyRepository companies;
    private final PasswordEncoder encoder;
    private final JwtUtil jwt;

    private final PasswordResetService passwordResetService;

    public AuthController(AdminUserRepository admins,
                          CompanyRepository companies,
                          PasswordEncoder encoder,
                          JwtUtil jwt,
                          PasswordResetService passwordResetService) {
        this.admins = admins;
        this.companies = companies;
        this.encoder = encoder;
        this.jwt = jwt;
        this.passwordResetService = passwordResetService;
    }

    // ================== REGISTER COMPANY ==================

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest req) {
        String email = req.getEmail() == null ? "" : req.getEmail().trim().toLowerCase();

        if (email.isBlank() || req.getPassword() == null || req.getPassword().isBlank()) {
            return ResponseEntity
                    .badRequest()
                    .body(ApiResponse.error("Email et mot de passe sont obligatoires"));
        }

        // Email déjà utilisé par un admin ?
        if (admins.findByEmail(email).isPresent()) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(ApiResponse.error("Cet email est déjà utilisé"));
        }

        // Email déjà utilisé par une entreprise ?
        if (companies.existsByEmail(email)) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(ApiResponse.error("Cet email est déjà utilisé"));
        }
        if (companies.existsByIce(req.getIce())) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(ApiResponse.error("Cet ICE est déjà utilisé"));
        }

        Company c = new Company();
        c.setName(req.getName());
        c.setEmail(email);
        c.setIce(req.getIce());
        c.setPassword(encoder.encode(req.getPassword())); // hash BCrypt
        c.setAddress(req.getAddress());
        c.setCity(req.getCity());
        c.setPhone(req.getPhone());
        c.setEnabled(true); // ou false si tu veux validation par admin

        companies.save(c);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Entreprise créée avec succès", null));
    }

    // ================== LOGIN ADMIN + COMPANY ==================

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
                    c.getId(),   // userId ou null si tu ne l’utilises pas
                    email,
                    "COMPANY",
                    c.getId()    // companyId dans les claims
            );
            var payload = new UserPayload(
                    c.getId(),
                    email,
                    "COMPANY",
                    c.getId()
            );
            return ResponseEntity.ok(new LoginResponse(token, payload, false));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of("error","SERVER_ERROR","detail",e.getClass().getSimpleName()));
        }
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        if (email == null || email.isBlank()) {
            return ResponseEntity
                    .badRequest()
                    .body(ApiResponse.error("Email est obligatoire"));
        }

        var tokenOpt = passwordResetService.createResetTokenForEmail(email);

        // Si tu avais un service d'email, tu enverrais ici
        tokenOpt.ifPresent(t -> {
            String link = "http://localhost:3000/reset-password?token=" + t;
            System.out.println("=== LIEN DE RÉINITIALISATION À ENVOYER PAR EMAIL ===");
            System.out.println(link);
            System.out.println("====================================================");
        });

        // Toujours 200, même si l'email n'existe pas
        return ResponseEntity.ok(
                ApiResponse.success("Si un compte existe avec cet email, un lien de réinitialisation a été envoyé.", null)
        );
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> body) {
        String token = body.get("token");
        String newPassword = body.get("newPassword");

        if (token == null || token.isBlank() || newPassword == null || newPassword.isBlank()) {
            return ResponseEntity
                    .badRequest()
                    .body(ApiResponse.error("Token et nouveau mot de passe sont obligatoires"));
        }

        try {
            passwordResetService.resetPassword(token, newPassword);
            return ResponseEntity.ok(
                    ApiResponse.success("Mot de passe réinitialisé avec succès", null)
            );
        } catch (IllegalArgumentException ex) {
            return ResponseEntity
                    .badRequest()
                    .body(ApiResponse.error(ex.getMessage()));
        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity
                    .status(500)
                    .body(ApiResponse.error("Erreur serveur lors de la réinitialisation du mot de passe"));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        return ResponseEntity.ok(
                Map.of("success", true, "message", "Déconnexion réussie")
        );
    }



}
