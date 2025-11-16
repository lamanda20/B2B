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

    private LocalDate dateCreation;

    @Transient
    private Double total;

    @PrePersist
    protected void onCreate() {
        if (dateCreation == null) {
            dateCreation = LocalDate.now();
        }
    }

    // Méthode ajouterProduit
    public void ajouterProduit(Produit produit, int quantite) {
        if (lignes == null) {
            lignes = new ArrayList<>();
        }
        LignePanier ligne = new LignePanier();
        ligne.setPanier(this);
        ligne.setProduit(produit);
        ligne.setQuantite(quantite);
        lignes.add(ligne);
    }

    // Méthode supprimerProduit
    public void supprimerProduit(Produit produit) {
        if (lignes != null) {
            lignes.removeIf(ligne -> ligne.getProduit().getId().equals(produit.getId()));
        }
    }

    // Méthode calculerTotal
    public double calculerTotal() {
        if (lignes == null || lignes.isEmpty()) {
            return 0.0;
        }
        return lignes.stream()
                .mapToDouble(LignePanier::getSousTotal)
                .sum();
    }

    // Getter pour total (calculé dynamiquement)
    public Double getTotal() {
        return calculerTotal();
    }

    // Méthode afficherContenu
    public void afficherContenu() {
        if (lignes == null || lignes.isEmpty()) {
            System.out.println("Le panier est vide.");
            return;
        }
        System.out.println("=== Contenu du Panier ===");
        lignes.forEach(LignePanier::afficherLigne);
        System.out.println("Total: " + calculerTotal() + " DH");
    }

    // Méthode validerCommande
    public Commande validerCommande() {
        if (lignes == null || lignes.isEmpty()) {
            throw new IllegalStateException("Le panier est vide");
        }

        Commande commande = new Commande();
        commande.setCompany(this.company);
        commande.setDateCommande(LocalDate.now());
        commande.setStatut(StatutCommande.EN_ATTENTE);

        // Copier les lignes du panier vers la commande
        for (LignePanier lignePanier : lignes) {
            LigneCommande ligneCommande = new LigneCommande();
            ligneCommande.setCommande(commande);
            ligneCommande.setProduit(lignePanier.getProduit());
            ligneCommande.setQuantite(lignePanier.getQuantite());
            ligneCommande.setPrixUnitaire(lignePanier.getProduit().getPrice().doubleValue());
            commande.ajouterLigne(ligneCommande);
        }

        return commande;
    }
}
