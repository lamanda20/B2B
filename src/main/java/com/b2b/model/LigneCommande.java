package com.b2b.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "lignes_commande")
@Data
public class LigneCommande {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "commande_id")
    private Commande commande;

    @ManyToOne
    @JoinColumn(name = "produit_id")
    private Produit produit;

    private int quantite;
    private double prixUnitaire;

    // Méthode pour calculer le sous-total
    public double getSousTotal() {
        return quantite * prixUnitaire;
    }

    // Méthode pour afficher la ligne
    public void afficherLigne() {
        System.out.println("Ligne de commande: " +
            (produit != null ? produit.getNom() : "N/A") +
            " - Quantité: " + quantite +
            " - Prix unitaire: " + prixUnitaire +
            " - Sous-total: " + getSousTotal());
    }
}
