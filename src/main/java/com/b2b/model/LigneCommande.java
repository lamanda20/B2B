package com.b2b.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "lignes_commande")
@Data
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class LigneCommande {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long idLigneCommande;

    @ManyToOne
    @JoinColumn(name = "commande_id")
    @JsonIgnoreProperties({"lignes", "livraison", "Company"})
    private Commande commande;

    @ManyToOne
    @JoinColumn(name = "produit_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "lignesCommande", "lignesPanier"})
    private Produit produit;

    private int quantite;
    private double prixUnitaire;

    // Attribut supplémentaire (probablement pour le vendeur)
    @Column(name = "seller_line_prix")
    private Double sellerLinePrix;

    // Méthode pour calculer le sous-total
    public double getSousTotal() {
        return quantite * prixUnitaire;
    }

    // Méthode pour afficher la ligne
    public void afficherLigne() {
        System.out.println("Ligne de commande: " +
            (produit != null ? produit.getName() : "N/A") +
            " - Quantité: " + quantite +
            " - Prix unitaire: " + prixUnitaire +
            " - Sous-total: " + getSousTotal());
    }
}
