package com.b2b.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "lignes_panier")
@Data
public class LignePanier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "panier_id")
    private Panier panier;

    @ManyToOne
    @JoinColumn(name = "produit_id")
    private Produit produit;

    private int quantite;
    private double prixUnitaire;

    // Méthode pour calculer le sous-total
    public double getSousTotal() {
        return quantite * prixUnitaire;
    }
}
