package com.b2b.controller;

import com.b2b.auth.LoginResponse;
import com.b2b.auth.UserPayload;
import com.b2b.security.JwtUtil;
import com.b2b.model.AppUser;
import com.b2b.repository.AppUserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final AppUserRepository userRepo;
    private final JwtUtil jwt;

    public AuthController(AuthenticationManager authenticationManager, AppUserRepository userRepo, JwtUtil jwt) {
        this.authenticationManager = authenticationManager;
        this.userRepo = userRepo;
        this.jwt = jwt;
    }

    // ↓↓↓ à mettre tout à la fin du fichier ↓↓↓
    static class AuthRequest {
        private String email;
        private String password;

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        System.out.println(">>> /api/auth/login email=" + request.getEmail());

        try {
            // 1) Auth Spring (vérifie email+mdp via UserDetailsService)
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );

            // 2) Charger l'utilisateur depuis la base
            AppUser u = userRepo.findByEmailIgnoreCase(request.getEmail())
                    .orElseThrow(() -> new IllegalStateException("User not found after auth"));

// ---- LOGS DIAG ----
            System.out.println("[LOGIN] u.id=" + u.getId());
            System.out.println("[LOGIN] u.email=" + u.getEmail());
            System.out.println("[LOGIN] u.role=" + (u.getRole() == null ? "null" : u.getRole().name()));
            System.out.println("[LOGIN] u.companyId=" + (u.getCompany() == null ? "null" : u.getCompany().getId()));
            System.out.println("[LOGIN] jwt bean is null? " + (jwt == null));

// 3) Construire le JWT (100% null-safe)
            Long userId   = (u.getId() != null) ? u.getId() : -1L;
            String email  = (u.getEmail() != null) ? u.getEmail() : "";
            String role   = (u.getRole() != null) ? u.getRole().name() : "SUPER_ADMIN"; // ou "UNKNOWN"
            Long companyId = (u.getCompany() != null && u.getCompany().getId() != null) ? u.getCompany().getId() : null;

            String token = jwt.generateToken(userId, email, role, companyId);

// 4) mustChangePassword peut être null → false par défaut
// adapte le getter selon ton entity (getMustChangePassword() ou isMustChangePassword())
            boolean mustChange = false;
            try {
                mustChange = Boolean.TRUE.equals(u.isMustChangePassword());
            } catch (Exception ignore) {
                // si ton getter est "isMustChangePassword()" (primitive)
                // enlève ce try/catch et fais : mustChange = u.isMustChangePassword();
            }

// Réponse OK
            var payload = new UserPayload(userId, email, role, companyId);
            return ResponseEntity.ok(new LoginResponse(token, payload, mustChange));




        } catch (org.springframework.security.authentication.BadCredentialsException e) {
            System.out.println(">>> BAD_CREDENTIALS");
            return ResponseEntity.status(401).body(Map.of("error", "BAD_CREDENTIALS"));
        } catch (org.springframework.security.authentication.DisabledException e) {
            System.out.println(">>> USER_DISABLED");
            return ResponseEntity.status(401).body(Map.of("error", "USER_DISABLED"));
        } catch (Exception e) {
            e.printStackTrace(); // <— important pour voir la vraie cause dans la console
            return ResponseEntity.status(500).body(Map.of("error", "SERVER_ERROR", "detail", e.getClass().getSimpleName()));
        }

    }
}
