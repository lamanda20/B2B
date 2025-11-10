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
    @JoinColumn(name = "user_id")
    @JsonIgnoreProperties({"livraisons", "commandes"})
    private User user;

    @OneToMany(mappedBy = "commande", cascade = CascadeType.ALL)
    @JsonIgnoreProperties({"commande"})
    private List<LigneCommande> lignes;

    @Enumerated(EnumType.STRING) // Important pour sauvegarder le nom du statut
    private StatutCommande statut;

    // --- VOTRE AJOUT ICI ---
    @OneToOne(cascade = CascadeType.ALL) // CascadeType.ALL signifie que si on sauvegarde une Commande, la Livraison associée l'est aussi.
    @JoinColumn(name = "livraison_id", referencedColumnName = "idLivraison") // Crée la clé étrangère
    @JsonIgnoreProperties({"commande", "user"})
    private Livraison livraison;

    // ... autres méthodes et attributs ...
}