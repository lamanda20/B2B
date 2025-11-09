package com.b2b.repository;

import com.b2b.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    // Aucune méthode personnalisée n'est requise pour votre tâche
}