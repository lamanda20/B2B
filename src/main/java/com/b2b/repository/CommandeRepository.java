package com.b2b.repository;

import com.b2b.model.Commande;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommandeRepository extends JpaRepository<Commande, Long> {

    // Permet de chercher une commande à partir de sa référence
    Optional<Commande> findByRefCommande(String refCommande);
}
