package com.b2b.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Entity
@Table(name = "lignes_commande")
@Data
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Slf4j
public class LigneCommande {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "commande_id")
    @JsonIgnoreProperties({"lignes", "livraison", "user"})
    private Commande commande;

    @ManyToOne
    @JoinColumn(name = "produit_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Produit produit;

    private int quantite;
    private double prixUnitaire;

    // Méthode pour calculer le sous-total
    public double getSousTotal() {
        return quantite * prixUnitaire;
    }

    // Méthode pour afficher la ligne
    public void afficherLigne() {
        log.info("Ligne de commande: {} - Quantité: {} - Prix unitaire: {} - Sous-total: {}",
                (produit != null ? produit.getName() : "N/A"),
                quantite,
                prixUnitaire,
                getSousTotal());
    }
}
