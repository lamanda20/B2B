package com.b2b.repository;

import com.b2b.model.AppUser;
import com.b2b.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {
    Optional<AppUser> findByEmailIgnoreCase(String email);
    boolean existsByEmailIgnoreCase(String email);
    long countByRole(com.b2b.model.Role role);
    long countByCompany(Company company);
}
