package com.b2b.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "produits")
@Data
@JsonIgnoreProperties({"lignesCommande", "lignesPanier", "hibernateLazyInitializer", "handler"})
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

    // Auto-référencement: un produit peut être une catégorie
    @ManyToOne
    @JoinColumn(name = "categorie_id")
    @JsonIgnoreProperties({"categorie", "company", "lignesCommande", "lignesPanier"})
    private Produit categorie;

    @OneToMany(mappedBy = "produit")
    @JsonIgnoreProperties({"produit"})
    private List<LigneCommande> lignesCommande;

    @OneToMany(mappedBy = "produit")
    @JsonIgnoreProperties({"produit"})
    private List<LignePanier> lignesPanier;
}
