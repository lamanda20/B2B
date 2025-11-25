package com.b2b.repository;

import com.b2b.model.Produit;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProduitRepository extends JpaRepository<Produit, Long> {
    List<Produit> findByNameContainingIgnoreCase(String q);
}
