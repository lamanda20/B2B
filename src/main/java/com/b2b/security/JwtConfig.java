package com.b2b.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtConfig {

    @Bean
    public JwtUtil jwtUtil(
            @Value("${security.jwt.secret:ChangeThisSecretOfAtLeast32Chars!!}") String secret,
            @Value("${security.jwt.issuer:b2b-application}") String issuer,
            @Value("${security.jwt.expiration:1440}") long expirationRaw
    ) {
        // The property may be provided as milliseconds (e.g. 86400000) or minutes.
        long expirationMinutes = expirationRaw;
        if (expirationRaw > 1000000L) { // likely milliseconds -> convert to minutes
            expirationMinutes = expirationRaw / 60000L;
        }
        return new JwtUtil(secret, issuer, expirationMinutes);
    }
}

