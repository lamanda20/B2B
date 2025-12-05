package com.b2b.repository;

import com.b2b.model.Commande;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CommandeRepository extends JpaRepository<Commande, Long> {

    Optional<Commande> findByRefCommande(String refCommande);

    List<Commande> findByCompanyId(Long companyId);   // buyer’s orders

    @Query("""
           SELECT DISTINCT c
           FROM Commande c
           JOIN c.lignes l
           WHERE l.produit.company.id = :sellerId
           """)
    List<Commande> findSellerOrders(@Param("sellerId") Long sellerId);

    @Query("""
       SELECT DISTINCT c 
       FROM Commande c 
       JOIN c.lignes l 
       WHERE l.produit.company.id = :sellerId
       """)
    List<Commande> findOrdersForSeller(Long sellerId);

}
