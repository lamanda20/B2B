package com.b2b.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ligne_commandes")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LigneCommande {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idLigneCommande;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "commande_id")
    private Commande commande;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "produit_id")
    private Produit produit;

    private int quantite;
    private double prixUnitaire;

    public double getSousTotal(){
        return quantite * prixUnitaire;
    }

    public void afficherLigne() {
        System.out.println(produit.getName() + " x " + quantite + " → " + getSousTotal() + " MAD");
    }
}
