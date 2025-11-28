package com.b2b.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class LignePanier {

    private int idlignePanier;
    private int quantite;
    private Panier panier;
    private Produit produit;

    public double getSousTotal() {
        if (produit != null && produit.getPrice() != 0) {
            BigDecimal total = BigDecimal.valueOf(produit.getPrice());

            return total.doubleValue(); // keeps old method signature
        }
        return 0.0;
    }
}
