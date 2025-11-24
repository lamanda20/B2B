package com.b2b.model;


import lombok.Data;

import java.math.BigDecimal;

public class Filter {
    private String nom;
    private String categorie;
    private BigDecimal prixMin;
    private BigDecimal prixMax;
    private String tri; // "asc" ou "desc"


    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public BigDecimal getPrixMin() {
        return prixMin;
    }

    public void setPrixMin(BigDecimal prixMin) {
        this.prixMin = prixMin;
    }

    public BigDecimal getPrixMax() {
        return prixMax;
    }

    public void setPrixMax(BigDecimal prixMax) {
        this.prixMax = prixMax;
    }

    public String getTri() {
        return tri;
    }

    public void setTri(String tri) {
        this.tri = tri;
    }
}
