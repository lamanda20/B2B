package com.b2b.service;

import com.b2b.model.AppUser;
import com.b2b.repository.AppUserRepository;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final AppUserRepository users;
    public CustomUserDetailsService(AppUserRepository users) { this.users = users; }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        AppUser u = users.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé: " + email));
        return User.builder()
                .username(u.getEmail())
                .password(u.getPassword())         // hash BCrypt en DB
                .roles(u.getRole().name())         // SUPER_ADMIN / COMPANY_ADMIN / BUYER / SELLER
                .disabled(!u.isEnabled())
                .build();
    }
}
