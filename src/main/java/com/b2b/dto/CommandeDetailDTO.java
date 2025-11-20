package com.b2b.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class CommandeDetailDTO {
    private Long id;
    private String refCommande;
    private LocalDate dateCommande;
    private String statut;
    private double total;

    private CompanyDto company;
    private DeliveryDTO livraison;
    private List<LigneCommandeDTO> lignes;
    private List<PaymentDTO> paiements;
}
