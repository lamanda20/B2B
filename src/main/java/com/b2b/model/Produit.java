package com.b2b.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "produits")
@Data
@JsonIgnoreProperties({"lignesCommande", "lignesPanier", "avis", "hibernateLazyInitializer", "handler"})
public class Produit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private BigDecimal price;
    private Integer stock;

    @ManyToOne
    @JoinColumn(name = "company_id")
    @JsonIgnoreProperties({"produits", "users"})
    private Company company;

    @ManyToOne
    @JoinColumn(name = "categorie_id", referencedColumnName = "id_cat")
    @JsonIgnoreProperties({"produits"})
    private Categorie categorie;

    @OneToMany(mappedBy = "produit", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties({"produit"})
    private List<Avis> avis;

    @OneToMany(mappedBy = "produit")
    @JsonIgnoreProperties({"produit"})
    private List<LigneCommande> lignesCommande;

    @OneToMany(mappedBy = "produit")
    @JsonIgnoreProperties({"produit"})
    private List<LignePanier> lignesPanier;
}
