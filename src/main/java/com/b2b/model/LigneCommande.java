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
    @Column(name = "id_ligne_commande")
    private Long idLigneCommande;

    @ManyToOne
    @JoinColumn(name = "commande_id")
    @JsonIgnoreProperties({"lignes", "livraison", "company", "paiements"})
    private Commande commande;

    @ManyToOne
    @JoinColumn(name = "produit_id")
    @JsonIgnoreProperties({"lignesCommande", "lignesPanier", "avis", "company", "categorie"})
    private Produit produit;

    private int quantite;
    private double prixUnitaire;

    @Column(name = "seller_line_prix")
    private Double sellerLinePrix;

    public double getSousTotal() {
        return quantite * prixUnitaire;
    }
}
