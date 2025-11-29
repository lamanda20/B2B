package com.b2b.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "produits")
@Data
public class Produit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private BigDecimal price;
    private int stock;
    private Long sellerId;


    @OneToMany(mappedBy = "produit")
    private List<LigneCommande> lignesCommande;

    // Relations expected by services/controllers
    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

    @ManyToOne
    @JoinColumn(name = "categorie_id")
    private Categorie categorie;

    // Compatibility getters used across the codebase
    public double getPrix() {
        return price != null ? price.doubleValue() : 0.0;
    }

    // Some services expect getCompany() and getCategorie()
    public Company getCompany() {
        return this.company;
    }

    public Categorie getCategorie() {
        return this.categorie;
    }
}
