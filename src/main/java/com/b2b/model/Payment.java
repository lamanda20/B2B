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

    private String mode;
    private String statut;

    @ManyToOne
    @JoinColumn(name = "app_user_id")
    @JsonIgnoreProperties({"payments", "panier", "company"})
    private AppUser appUser;

    private String product;
    private LocalDate date;
    private Double amount;

    @Enumerated(EnumType.STRING)
    private StatutPaiement status;

    @ManyToOne
    @JoinColumn(name = "commande_id")
    @JsonIgnoreProperties({"payments", "lignes", "livraison"})
    private Commande commande;

    // Méthodes métier
    public boolean effectuerPaiement() {
        // Logique de paiement
        if (this.amount != null && this.amount > 0) {
            this.status = StatutPaiement.PAYE;
            this.statut = "PAYE";
            return true;
        }
        this.status = StatutPaiement.ECHOUE;
        this.statut = "ECHOUE";
        return false;
    }

    public String getStatutPaiement() {
        return this.status != null ? this.status.name() : "INCONNU";
    }

    public double calculerMontant() {
        return this.amount != null ? this.amount : 0.0;
    }
}
