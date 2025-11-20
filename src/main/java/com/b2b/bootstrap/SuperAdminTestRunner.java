package com.b2b.bootstrap;

            import com.b2b.model.Company;
            import com.b2b.repository.CompanyRepository;
            import com.b2b.service.CustomUserDetailsService;
            import org.springframework.boot.CommandLineRunner;
            import org.springframework.context.annotation.Profile;
            import org.springframework.core.annotation.Order;
            import org.springframework.security.crypto.password.PasswordEncoder;
            import org.springframework.stereotype.Component;

            @Component
            @Profile({"test"})
            @Order(2)
            public class SuperAdminTestRunner implements CommandLineRunner {

                private final CompanyRepository userRepository;
                private final CustomUserDetailsService userDetailsService;
                private final PasswordEncoder passwordEncoder;

                public SuperAdminTestRunner(CompanyRepository userRepository,
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

                    Company user = userRepository.findByEmailIgnoreCase(email)
                            .orElse(null);
                    if (user == null) {
                        System.out.println("❌ SuperAdmin non trouvé en base !");
                        return;
                    }

                    System.out.println("✅ SuperAdmin trouvé en base : " + user.getEmail());
                    System.out.println("Mot de passe hashé en DB : " + user.getPassword());

                    boolean matches = passwordEncoder.matches(rawPassword, user.getPassword());
                    System.out.println("Password matches: " + matches);

                    try {
                        var userDetails = userDetailsService.loadUserByUsername(email);
                        System.out.println("UserDetailsService OK : " + userDetails.getUsername());
                    } catch (Exception e) {
                        System.out.println("❌ UserDetailsService erreur : " + e.getMessage());
                    }
                }
            }