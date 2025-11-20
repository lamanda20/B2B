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
    @Column(unique = true)
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

    @PrePersist
    protected void onCreate() {
        if (dateCommande == null) dateCommande = LocalDate.now();
        if (refCommande == null) refCommande = "CMD-" + System.currentTimeMillis();
    }

    public void ajouterLigne(LigneCommande ligne) {
        lignes.add(ligne);
        ligne.setCommande(this);
    }

    public double calculerTotal() {
        double total = lignes.stream()
                .mapToDouble(LigneCommande::getSousTotal)
                .sum();

        if (livraison != null) total += livraison.getFraisLivraison();

        return total;
    }
}
