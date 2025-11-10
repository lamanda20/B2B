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
public class Commande {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String refCommande;

    private LocalDate dateCommande;

    @Enumerated(EnumType.STRING)
    private StatutCommande statut = StatutCommande.EN_COURS;

    // ============================
    // RELATION AVEC L'UTILISATEUR
    // ============================
    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnoreProperties({"livraisons", "commandes"})
    private User user;

    // ============================
    // RELATION AVEC LIGNECOMMANDE
    // ============================
    @OneToMany(mappedBy = "commande", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties({"commande"})
    private List<LigneCommande> lignes = new ArrayList<>();

    // ============================
    // RELATION AVEC LIVRAISON
    // ============================
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "livraison_id", referencedColumnName = "idLivraison")
    @JsonIgnoreProperties({"commande", "user"})
    private Livraison livraison;

    // ============================
    // MÉTHODES MÉTIER
    // ============================

    public void ajouterLigne(LigneCommande ligneCmd) {
        ligneCmd.setCommande(this);     // association bidirectionnelle
        lignes.add(ligneCmd);
    }

    public double calculerTotal() {
        return lignes.stream()
                .mapToDouble(LigneCommande::getSousTotal)
                .sum();
    }

    public StatutCommande suivreCommande() {
        return this.getStatut();
    }

    public void afficherCommande() {
        System.out.println("Commande numéro :" + id + " effectuée le :" + dateCommande);
        System.out.println("Statut : " + statut);
        System.out.println("-----------------------------------------------");

        for (LigneCommande lc : lignes) {
            System.out.println("Produit :" + lc.getProduit().getNom()
                    + " | quantité: " + lc.getQuantite()
                    + " | prix unitaire : " + lc.getPrixUnitaire()
                    + " | sous-total : " + lc.getSousTotal());
        }

        System.out.println("Total : " + calculerTotal() + " DH");
    }
}
