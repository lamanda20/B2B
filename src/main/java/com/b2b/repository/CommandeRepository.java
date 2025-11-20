package com.b2b.repository;

import com.b2b.model.Commande;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommandeRepository extends JpaRepository<Commande, Long> {
    // On aura besoin de cette méthode pour votre interface de suivi
    Optional<Commande> findByRefCommande(String refCommande);
    List<Commande> findByCompanyId(Long CompanyId);
}