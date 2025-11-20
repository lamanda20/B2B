package com.b2b.service;

        import com.b2b.model.Company;
        import com.b2b.repository.CompanyRepository;
        import org.springframework.security.core.userdetails.*;
        import org.springframework.stereotype.Service;

        @Service
        public class CustomUserDetailsService implements UserDetailsService {
            private final CompanyRepository companies;

            public CustomUserDetailsService(CompanyRepository companies) {
                this.companies = companies;
            }

            @Override
            public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
                Company u = companies.findByEmailIgnoreCase(email)
                        .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé: " + email));
                return User.builder()
                        .username(u.getEmail())
                        .password(u.getPassword()) // hash BCrypt en DB
                        .roles("COMPANY") // Adjust if you have a role field
                        .disabled(false) // Adjust if you have an enabled field
                        .build();
            }
        }