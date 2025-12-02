package com.b2b.repository;

import com.b2b.model.Commande;
import com.b2b.model.LigneCommande;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LigneCommandeRepository extends JpaRepository<LigneCommande, Long> {
    // On aura besoin de cette méthode pour votre interface de suivi
    Optional<LigneCommande> findByIdLigneCommande(Long id);
    List<LigneCommande> findByCommandeId(Long commandeId);
}