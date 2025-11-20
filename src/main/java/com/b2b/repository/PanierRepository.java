package com.b2b.repository;

import com.b2b.model.Panier;
import com.b2b.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PanierRepository extends JpaRepository<Panier, Long> {
    Panier findByCompany(Company company);
}
