package com.b2b.service;

import com.b2b.model.Commande;
import com.b2b.model.LigneCommande;
import com.b2b.model.StatutCommande;

import java.util.List;
import java.util.Optional;

public interface CommandeService {
    List<Commande> findAll();
    Optional<Commande> findById(Long id);
    Optional<Commande> findByRefCommande(String refCommande);
    List<Commande> findByCompany(Long companyId);
    Commande create(Commande commande);
    Commande ajouterLigneCommande(Long commandeId, LigneCommande ligne);
    double calculerTotal(Long commandeId);
    StatutCommande validerCommande(Long commandeId);
    void afficherCommande(Long commandeId);
    Commande updateStatut(Long commandeId, StatutCommande statut);
    void delete(Long id);
}
