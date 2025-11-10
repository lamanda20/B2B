package com.b2b.model;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LignePanier {
    private int idlignePanier;
    private int quantite;
    private Panier panier;
    private Produit produit;
    public double getSousTotal() {
       if(produit != null){
           return produit.getPrix() * quantite ;
       }
       return 0.0 ;
    }
}
