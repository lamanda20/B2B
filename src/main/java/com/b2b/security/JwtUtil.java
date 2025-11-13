package com.b2b.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;               // <-- important
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.Map;

public class JwtUtil {
    private final SecretKey key;            // <-- SecretKey (pas Key)
    private final String issuer;
    private final long expirationMinutes;

    public JwtUtil(String secret, String issuer, long expirationMinutes) {
        // secret >= 32 chars pour HS256
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.issuer = issuer;
        this.expirationMinutes = expirationMinutes;
    }

    // Génération (JJWT 0.12.x)
    public String generateToken(Long userId, String email, String role, Long companyId) {
        Instant now = Instant.now();

        var builder = Jwts.builder()
                .subject(email)
                .issuer(issuer)
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusSeconds(expirationMinutes * 60)))
                .claim("uid", userId)   // jamais null
                .claim("role", role);   // assure-toi que 'role' n'est pas null avant d'appeler

        // N'AJOUTE companyId QUE s'il n'est pas null
        if (companyId != null) {
            builder.claim("companyId", companyId);
        }

        return builder.signWith(key).compact();
    }


    // Parsing/Validation (JJWT 0.12.x)
    public Jws<Claims> parse(String token) {
        JwtParser parser = Jwts.parser()
                .verifyWith(key)                    // <-- une seule arg: SecretKey
                .build();
        return parser.parseSignedClaims(token);
    }
}
