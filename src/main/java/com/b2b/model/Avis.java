package com.b2b.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Table(name = "avis")
@Data
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Avis {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idAvis;

    @Column(columnDefinition = "TEXT")
    private String feedback;

    @Column(nullable = false)
    private int note;

    @Column(name = "date_creation")
    private LocalDate dateCreation;

    @ManyToOne
    @JoinColumn(name = "company_id")
    @JsonIgnoreProperties({"commandes", "panier", "payments", "company", "avisList"})
    private Company company;

    @ManyToOne
    @JoinColumn(name = "produit_id", nullable = false)
    @JsonIgnoreProperties({"avis", "lignesCommande", "lignesPanier", "categorie"})
    private Produit produit;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EtatAvis etat;

    @PrePersist
    protected void onCreate() {
        if (dateCreation == null) {
            dateCreation = LocalDate.now();
        }
        if (etat == null) {
            etat = EtatAvis.EN_ATTENTE;
        }
    }

    public static Avis ajouterAvis(Produit produit, Company company, String feedback, int note) {
        Avis avis = new Avis();
        avis.setProduit(produit);
        avis.setCompany(company);
        avis.setFeedback(feedback);
        avis.setNote(note);
        avis.setDateCreation(LocalDate.now());
        avis.setEtat(EtatAvis.EN_ATTENTE);
        return avis;
    }

    public Avis supprimerAvis() {
        return this;
    }
}
