package com.b2b.repository;

import com.b2b.model.Company;
import com.b2b.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CompanyRepository extends JpaRepository<Company, Long> {
    Optional<Company> findByNameIgnoreCase(String name);
    boolean existsByNameIgnoreCase(String name);
    Optional<Company> findByEmailIgnoreCase(String email);

    // Méthodes alias attendues par le code existant
    boolean existsByEmail(String email);
    Optional<Company> findByEmail(String email);

    boolean existsByIce(String ice);
}