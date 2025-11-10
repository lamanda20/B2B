package com.b2b.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Panier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Le propriétaire du panier
    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnoreProperties({"commandes", "livraisons"})
    private User user;

    @OneToMany(mappedBy = "panier", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties({"panier"})
    private List<LignePanier> lignes = new ArrayList<>();

    private LocalDate dateCreation = LocalDate.now();

    // =============================
    // MÉTHODES MÉTIER
    // =============================

    public void ajouterProduit(Produit produit, int quantite) {
        for (LignePanier lp : lignes) {
            if (lp.getProduit().getId().equals(produit.getId())) {
                lp.setQuantite(lp.getQuantite() + quantite);
                return;
            }
        }

        LignePanier nouvelleLigne = new LignePanier();
        nouvelleLigne.setPanier(this);
        nouvelleLigne.setProduit(produit);
        nouvelleLigne.setQuantite(quantite);

        lignes.add(nouvelleLigne);
    }

    public void supprimerProduit(Produit p) {
        lignes.removeIf(lp -> {
            if (lp.getProduit().getId().equals(p.getId())) {
                if (lp.getQuantite() > 1) {
                    lp.setQuantite(lp.getQuantite() - 1);
                    return false;
                } else {
                    return true;
                }
            }
            return false;
        });
    }

    public double calculerTotal() {
        return lignes.stream()
                .mapToDouble(LignePanier::getSousTotal)
                .sum();
    }

    public void viderPanier() {
        lignes.clear();
    }

    public Commande validerCommande() {

        if (lignes.isEmpty()) {
            return null;
        }

        Commande commande = new Commande();
        commande.setDateCommande(LocalDate.now());
        commande.setStatut(StatutCommande.EN_COURS);
        commande.setUser(this.user);

        List<LigneCommande> lignesCommande = new ArrayList<>();
        commande.setLignes(lignesCommande);

        for (LignePanier lp : lignes) {
            LigneCommande lc = new LigneCommande();
            lc.setCommande(commande);
            lc.setProduit(lp.getProduit());
            lc.setQuantite(lp.getQuantite());
            lc.setPrixUnitaire(lp.getProduit().getPrix());
            lignesCommande.add(lc);
        }

        viderPanier();

        return commande;
    }

}
