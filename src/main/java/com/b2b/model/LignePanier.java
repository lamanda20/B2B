package com.b2b.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "lignes_panier")
@Data
public class LignePanier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long idLignePanier;

    @ManyToOne
    @JoinColumn(name = "panier_id")
    private Panier panier;

    @ManyToOne
    @JoinColumn(name = "produit_id")
    private Produit produit;

    private int quantite;

    // Méthode pour calculer le sous-total
    public double getSousTotal() {
        if (produit != null && produit.getPrice() != null) {
            return quantite * produit.getPrice().doubleValue();
        }
        return 0.0;
    }

    // Méthode pour afficher la ligne
    public void afficherLigne() {
        System.out.println("Ligne de panier: " +
            (produit != null ? produit.getName() : "N/A") +
            " - Quantité: " + quantite +
            " - Sous-total: " + getSousTotal());
    }
}
