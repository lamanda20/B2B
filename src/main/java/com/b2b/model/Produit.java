package com.b2b.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Entity
@Table(name = "produits")
@Data
public class Produit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;
    private String description;
    private double prix;
    private int stockDisponible;
    private String categorie;

    @OneToMany(mappedBy = "produit")
    private List<LigneCommande> lignesCommande;
}
