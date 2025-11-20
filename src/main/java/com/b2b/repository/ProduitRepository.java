package com.b2b.repository;

import com.b2b.model.Produit;
import com.b2b.model.Categorie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ProduitRepository extends JpaRepository<Produit, Long> {

    // Recherche par mot-clé dans le nom ou description
    @Query("SELECT p FROM Produit p WHERE " +
            "LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(p.description) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Produit> findByKeyword(@Param("keyword") String keyword);

    // Filtre par catégorie
    List<Produit> findByCategorie(Categorie categorie);

    // Filtre par prix entre min et max
    List<Produit> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice);

    // Filtre par stock minimum
    List<Produit> findByStockGreaterThanEqual(Integer minStock);

    // Filtre par compagnie
    List<Produit> findByCompanyId(Long companyId);

    // Recherche avancée avec tous les filtres
    @Query("SELECT p FROM Produit p WHERE " +
            "(:keyword IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(p.description) LIKE LOWER(CONCAT('%', :keyword, '%'))) AND " +
            "(:categorie IS NULL OR p.categorie = :categorie) AND " +
            "(:minPrice IS NULL OR p.price >= :minPrice) AND " +
            "(:maxPrice IS NULL OR p.price <= :maxPrice) AND " +
            "(:minStock IS NULL OR p.stock >= :minStock) AND " +
            "(:companyId IS NULL OR p.companyId = :companyId)")
    List<Produit> findByAdvancedFilters(
            @Param("keyword") String keyword,
            @Param("categorie") Categorie categorie,
            @Param("minPrice") BigDecimal minPrice,
            @Param("maxPrice") BigDecimal maxPrice,
            @Param("minStock") Integer minStock,
            @Param("companyId") Long companyId);

    // Recherche par nom (exact match)
    List<Produit> findByNameContainingIgnoreCase(String name);
}