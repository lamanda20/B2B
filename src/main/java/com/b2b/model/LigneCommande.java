package com.b2b.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LigneCommande {
    private int idLigneCommande;
    private Commande commande;
    private Produit produit;
    private int quantite;
    private double prixUnitaire;

    public double getSousTotal(){
        return quantite * prixUnitaire;
    }
    public void afficherLigne() {
        System.out.println(produit.getNom() + " x " + quantite + " → " + getSousTotal() + " MAD");
    }
}
