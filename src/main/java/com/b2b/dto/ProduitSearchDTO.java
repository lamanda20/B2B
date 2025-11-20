package com.b2b.dto;

import java.math.BigDecimal;

public class ProduitSearchDTO {
    private String keyword;
    private Long categorieId;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private Integer minStock;
    private String sortBy;
    private String sortDirection;

    // ✅ CONSTRUCTEUR SANS ARGUMENTS
    public ProduitSearchDTO() {
    }

    // ✅ CONSTRUCTEUR POUR TESTS
    public ProduitSearchDTO(String keyword, Long categorieId, BigDecimal minPrice,
                            BigDecimal maxPrice, Integer minStock) {
        this.keyword = keyword;
        this.categorieId = categorieId;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        this.minStock = minStock;
    }

    // ✅ GETTERS ET SETTERS
    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public Long getCategorieId() {
        return categorieId;
    }

    public void setCategorieId(Long categorieId) {
        this.categorieId = categorieId;
    }

    public BigDecimal getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(BigDecimal minPrice) {
        this.minPrice = minPrice;
    }

    public BigDecimal getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(BigDecimal maxPrice) {
        this.maxPrice = maxPrice;
    }

    public Integer getMinStock() {
        return minStock;
    }

    public void setMinStock(Integer minStock) {
        this.minStock = minStock;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public String getSortDirection() {
        return sortDirection;
    }

    public void setSortDirection(String sortDirection) {
        this.sortDirection = sortDirection;
    }

    // ✅ MÉTHODE POUR VÉRIFIER SI DES FILTRES SONT APPLIQUÉS
    public boolean hasFilters() {
        return (keyword != null && !keyword.trim().isEmpty()) ||
                categorieId != null ||
                minPrice != null ||
                maxPrice != null ||
                minStock != null;
    }

    @Override
    public String toString() {
        return "ProduitSearchDTO{" +
                "keyword='" + keyword + '\'' +
                ", categorieId=" + categorieId +
                ", minPrice=" + minPrice +
                ", maxPrice=" + maxPrice +
                ", minStock=" + minStock +
                ", sortBy='" + sortBy + '\'' +
                ", sortDirection='" + sortDirection + '\'' +
                '}';
    }
}