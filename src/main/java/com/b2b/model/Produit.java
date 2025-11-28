package com.b2b.model;

import com.b2b.model.Categorie;
import com.b2b.model.Company;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data
@NoArgsConstructor

public class Produit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String imageUrl;
    private String name;
    private String description;

    private double price;
    private int stock;

    @ManyToOne
    private Company company;

    @ManyToOne
    private Categorie categorie;

    private String filterTag;

    public Produit(
            Long id,
            String imageUrl,
            String name,
            String description,
            double price,
            int stock,
            Company company,
            Categorie categorie,
            String filterTag
    ) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.name = name;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.company = company;
        this.categorie = categorie;
        this.filterTag = filterTag;
    }


}
