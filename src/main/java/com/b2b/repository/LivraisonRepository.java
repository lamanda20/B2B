package com.b2b.repository;

import com.b2b.model.Livraison;
import com.b2b.model.StatutCommande;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LivraisonRepository extends JpaRepository<Livraison, Long> {
    // Spring Data JPA va générer automatiquement les méthodes
    // comme save(), findById(), findAll(), etc.

    // Méthode pour trouver une livraison par l'ID de la commande
    Optional<Livraison> findByCommandeId(Long commandeId);

    // Méthodes pour les recherches par statut de commande
    @Query("SELECT l FROM Livraison l WHERE l.commande.statut = :statut")
    List<Livraison> findByStatut(@Param("statut") StatutCommande statut);

    // Recherche par ville
    List<Livraison> findByVille(String ville);

    // Recherche par transporteur
    List<Livraison> findByTransporteur(String transporteur);

    // Recherche par user
    @Query("SELECT l FROM Livraison l WHERE l.user.id = :userId")
    List<Livraison> findByUserId(@Param("userId") Long userId);
}
