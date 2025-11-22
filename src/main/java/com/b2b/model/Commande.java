package com.b2b.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "commandes")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Commande {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String refCommande;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

    @OneToMany(mappedBy = "commande", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LigneCommande> lignes = new ArrayList<>();

    private LocalDate dateCommande;

    @Enumerated(EnumType.STRING)
    private StatutCommande statut = StatutCommande.EN_COURS;

    @OneToOne
    @JoinColumn(name = "livraison_id")
    private Livraison livraison;



    public void ajouterligne(LigneCommande lignecmd) {
        if (lignecmd == null) return;
        lignecmd.setCommande(this);
        lignes.add(lignecmd);
    }

    public double calculerTotal() {
        return lignes.stream()
                .mapToDouble(LigneCommande::getSousTotal)
                .sum();
    }

    public StatutCommande suivreCommande() {
        return this.statut;
    }

    public void afficherCommande() {
        System.out.println("Commande numéro :" + id + " effectuée le :" + dateCommande);
        if (company != null) {
            System.out.println("Company :" + company.getName());
        }
        System.out.println("Statut :" + statut);
        System.out.println("-----------------------------------------------");
        for (LigneCommande lc : lignes) {
            System.out.println("Produit :" + lc.getProduit().getName()
                    + " | quantité: " + lc.getQuantite()
                    + " | prix unitaire : " + lc.getPrixUnitaire()
                    + " | sous-total : " + lc.getSousTotal());
        }
        System.out.println("Total : " + calculerTotal() + " DH");
    }

    public void ajouterLigne(LigneCommande ligne){
        if (ligne == null) return;
        ligne.setCommande(this);
        lignes.add(ligne);
    }


    public StatutCommande validerCommande() {
        this.statut = StatutCommande.VALIDEE;
        return this.statut;
    }
}
