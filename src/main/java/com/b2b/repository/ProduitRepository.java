package com.b2b.repository;

import com.b2b.model.Produit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProduitRepository extends JpaRepository<Produit, Long> {
    List<Produit> findByCompanyId(Long companyId);
    List<Produit> findByCategorieIdCat(Integer categorieId);
    List<Produit> findByNameContainingIgnoreCase(String name);
    List<Produit> findByStockGreaterThan(Integer stock);
}
