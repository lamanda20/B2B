package com.b2b.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "paniers")
@Data
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Panier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "company_id")
    @JsonIgnoreProperties({"produits"})
    private Company company;

    @OneToMany(mappedBy = "panier", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties({"panier"})
    private List<LignePanier> lignes = new ArrayList<>();

    private LocalDate dateCreation = LocalDate.now();

    @Transient
    private Double total;

    public void ajouterProduit(Produit produit, int quantite) {
        LignePanier ligne = new LignePanier();
        ligne.setPanier(this);
        ligne.setProduit(produit);
        ligne.setQuantite(quantite);
        lignes.add(ligne);
    }

    public void supprimerProduit(Produit produit) {
        lignes.removeIf(ligne -> ligne.getProduit().getId().equals(produit.getId()));
    }

    public double calculerTotal() {
        return lignes.stream()
                .mapToDouble(LignePanier::getSousTotal)
                .sum();
    }

    public Double getTotal() {
        return calculerTotal();
    }

    public Commande validerCommande() {
        if (lignes.isEmpty()) {
            throw new IllegalStateException("Le panier est vide");
        }

        Commande commande = new Commande();
        commande.setCompany(this.company);
        commande.setDateCommande(LocalDate.now());
        commande.setStatut(StatutCommande.EN_ATTENTE);

        for (LignePanier lp : lignes) {
            LigneCommande lcmd = new LigneCommande();
            lcmd.setCommande(commande);
            lcmd.setProduit(lp.getProduit());
            lcmd.setQuantite(lp.getQuantite());
            lcmd.setPrixUnitaire(lp.getProduit().getPrice().doubleValue());
            commande.ajouterLigne(lcmd);
        }

        return commande;
    }
}
