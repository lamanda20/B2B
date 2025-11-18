package com.b2b.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.util.ArrayList;
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
    @JoinColumn(name = "company_id")
    @JsonIgnoreProperties({"produits"})
    private Company company;

    @OneToMany(mappedBy = "commande", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties({"commande"})
    private List<LigneCommande> lignes = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private StatutCommande statut;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "livraison_id", referencedColumnName = "idLivraison")
    @JsonIgnoreProperties({"commande"})
    private Livraison livraison;

    @OneToMany(mappedBy = "commande", cascade = CascadeType.ALL)
    @JsonIgnoreProperties({"commande", "company"})
    private List<Payment> paiements = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        if (dateCommande == null) {
            dateCommande = LocalDate.now();
        }
        if (refCommande == null) {
            refCommande = "CMD-" + System.currentTimeMillis();
        }
    }

    // Méthode ajouterLigneCommande
    public void ajouterLigneCommande(LigneCommande ligne) {
        if (lignes == null) {
            lignes = new ArrayList<>();
        }
        lignes.add(ligne);
        ligne.setCommande(this);
    }

    // Alias ajouterLigne
    public void ajouterLigne(LigneCommande ligne) {
        ajouterLigneCommande(ligne);
    }

    // Méthode calculerTotal
    public double calculerTotal() {
        double total = 0.0;
        if (lignes != null && !lignes.isEmpty()) {
            total = lignes.stream()
                    .mapToDouble(LigneCommande::getSousTotal)
                    .sum();
        }
        if (livraison != null) {
            total += livraison.getFraisLivraison();
        }
        return total;
    }

    // Méthode validerCommande
    public StatutCommande validerCommande() {
        this.statut = StatutCommande.VALIDEE;
        return this.statut;
    }

    // Méthode afficherCommande
    public void afficherCommande() {
        System.out.println("=== Commande #" + refCommande + " ===");
        System.out.println("Date: " + dateCommande);
        System.out.println("Statut: " + statut);
        System.out.println("Company: " + (company != null ? company.getName() : "N/A"));
        System.out.println("--- Lignes de commande ---");
        if (lignes != null && !lignes.isEmpty()) {
            lignes.forEach(LigneCommande::afficherLigne);
        }
        System.out.println("Total: " + calculerTotal() + " DH");
    }
}