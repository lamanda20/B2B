package com.b2b.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "commandes")
@Data
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Commande {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String refCommande;
    private LocalDate dateCommande;

    @ManyToOne
    @JoinColumn(name = "client_id")
    @JsonIgnoreProperties({"commandes", "panier", "payments", "company"})
    private AppUser client;

    @OneToMany(mappedBy = "commande", cascade = CascadeType.ALL)
    @JsonIgnoreProperties({"commande"})
    private List<LigneCommande> lignes;

    @Enumerated(EnumType.STRING)
    private StatutCommande statut;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "livraison_id", referencedColumnName = "idLivraison")
    @JsonIgnoreProperties({"commande"})
    private Livraison livraison;

    @OneToMany(mappedBy = "commande")
    @JsonIgnoreProperties({"commande", "appUser"})
    private List<Payment> payments;

    // Méthodes métier
    public void ajouterLigne(LigneCommande ligne) {
        if (lignes != null) {
            lignes.add(ligne);
            ligne.setCommande(this);
        }
    }

    public void validerCmd() {
        this.statut = StatutCommande.VALIDEE;
    }

    public StatutCommande suiviCommande() {
        return this.statut;
    }

    public void setAfficherCommande() {
        System.out.println("Commande #" + refCommande);
        System.out.println("Date: " + dateCommande);
        System.out.println("Statut: " + statut);
        if (lignes != null) {
            lignes.forEach(LigneCommande::afficherLigne);
        }
    }
}