package com.b2b.dto;

import java.math.BigDecimal;

public class ProduitDTO {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer stock;
    private Long companyId;
    private String categorieName;
    private Long categorieId;

    // ✅ CONSTRUCTEUR SANS ARGUMENTS (OBLIGATOIRE)
    public ProduitDTO() {
    }

    // ✅ CONSTRUCTEUR AVEC ARGUMENTS (OPTIONNEL)
    public ProduitDTO(Long id, String name, String description, BigDecimal price,
                      Integer stock, Long companyId, String categorieName, Long categorieId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.companyId = companyId;
        this.categorieName = categorieName;
        this.categorieId = categorieId;
    }

    // ✅ GETTERS ET SETTERS (TRÈS IMPORTANTS)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public String getCategorieName() {
        return categorieName;
    }

    public void setCategorieName(String categorieName) {
        this.categorieName = categorieName;
    }

    public Long getCategorieId() {
        return categorieId;
    }

    public void setCategorieId(Long categorieId) {
        this.categorieId = categorieId;
    }

    // ✅ MÉTHODE toString() POUR LE DÉBOGAGE
    @Override
    public String toString() {
        return "ProduitDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", stock=" + stock +
                ", categorieName='" + categorieName + '\'' +
                '}';
    }
}