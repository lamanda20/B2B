package com.b2b.dto;

public class LigneCommandeDto {

    private Long idLigneCommande;
    private ProduitLite produit;
    private int quantite;
    private double prixUnitaire;

    public Long getIdLigneCommande() { return idLigneCommande; }
    public void setIdLigneCommande(Long idLigneCommande) { this.idLigneCommande = idLigneCommande; }

    public ProduitLite getProduit() { return produit; }
    public void setProduit(ProduitLite produit) { this.produit = produit; }

    public int getQuantite() { return quantite; }
    public void setQuantite(int quantite) { this.quantite = quantite; }

    public double getPrixUnitaire() { return prixUnitaire; }
    public void setPrixUnitaire(double prixUnitaire) { this.prixUnitaire = prixUnitaire; }

    public double getSousTotal() { return quantite * prixUnitaire; }

    public static LigneCommandeDto fromEntity(com.b2b.model.LigneCommande lc) {
        LigneCommandeDto dto = new LigneCommandeDto();
        dto.setIdLigneCommande(lc.getIdLigneCommande());
        dto.setQuantite(lc.getQuantite());
        dto.setPrixUnitaire(lc.getPrixUnitaire());

        ProduitLite p = new ProduitLite();
        p.setId(lc.getProduit().getId());
        p.setName(lc.getProduit().getName());
        p.setImageUrl(lc.getProduit().getImageUrl());
        dto.setProduit(p);

        return dto;
    }

    public static class ProduitLite {
        private Long id;
        private String name;
        private String imageUrl;

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public String getImageUrl() { return imageUrl; }
        public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    }
}
