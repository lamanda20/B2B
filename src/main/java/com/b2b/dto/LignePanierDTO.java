package com.b2b.dto;

import lombok.Data;

@Data
public class LignePanierDTO {
    private Long id;
    private String produitName;
    private int quantite;
    private double sousTotal;
}
