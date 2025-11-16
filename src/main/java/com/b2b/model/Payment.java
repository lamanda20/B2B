package com.b2b.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Table(name = "payments")
@Data
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String moyen;
    private String produit;

    @ManyToOne
    @JoinColumn(name = "company_id")
    @JsonIgnoreProperties({"produits"})
    private Company company;

    private LocalDate date;
    private Double amount;

    @Enumerated(EnumType.STRING)
    private StatutPaiement status;

    @ManyToOne
    @JoinColumn(name = "commande_id")
    @JsonIgnoreProperties({"paiements", "lignes", "livraison", "company"})
    private Commande commande;

    @PrePersist
    protected void onCreate() {
        if (date == null) {
            date = LocalDate.now();
        }
        if (status == null) {
            status = StatutPaiement.EN_ATTENTE;
        }
    }

    // Méthode effectuerPaiement
    public boolean effectuerPaiement() {
        if (this.amount != null && this.amount > 0) {
            this.status = StatutPaiement.PAYE;
            return true;
        }
        this.status = StatutPaiement.ECHOUE;
        return false;
    }

    // Méthode getStatutPaiement
    public String getStatutPaiement() {
        return status != null ? status.name() : "INCONNU";
    }

    // Méthode calculerMontant
    public double calculerMontant() {
        return amount != null ? amount : 0.0;
    }
}
