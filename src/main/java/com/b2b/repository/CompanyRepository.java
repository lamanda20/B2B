package com.b2b.repository;

import com.b2b.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CompanyRepository extends JpaRepository<Company, Long> {

    Optional<Company> findByEmail(String email);

    // Tu peux aussi garder cette version si tu veux ignorer la casse :
    Optional<Company> findByEmailIgnoreCase(String email);

    boolean existsByEmail(String email);
}
