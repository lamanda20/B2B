package com.b2b.dto;

import lombok.Data;

@Data
public class LigneCommandeDTO {
    private Long id;
    private String produitName;
    private int quantite;
    private double prixUnitaire;
    private double sousTotal;
}
