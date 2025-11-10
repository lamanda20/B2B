package com.b2b.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class LigneCommande {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // =============================
    // RELATION AVEC COMMANDE
    // =============================
    @ManyToOne
    @JoinColumn(name = "commande_id")
    @JsonIgnoreProperties({"lignes", "livraison", "user"})
    private Commande commande;

    // =============================
    // RELATION AVEC PRODUIT
    // =============================
    @ManyToOne
    @JoinColumn(name = "produit_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Produit produit;

    private int quantite;
    private double prixUnitaire;

    // =============================
    // MÉTHODES MÉTIER
    // =============================
    public double getSousTotal() {
        return quantite * prixUnitaire;
    }

    public void afficherLigne() {
        System.out.println(produit.getNom() + " x " + quantite + " → " + getSousTotal() + " MAD");
    }
}
