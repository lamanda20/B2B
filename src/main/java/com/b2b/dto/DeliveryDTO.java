package com.b2b.dto;

import com.b2b.model.StatutCommande;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * DTO pour les réponses de l'API Deliveries
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryDTO {
    private Long id;
    private String adresse;
    private String ville;
    private String telephone;
    private String transporteur;
    private Double fraisLivraison;
    private LocalDate dateEnvoi;
    private LocalDate dateEstimee;
    private StatutCommande statut;
    private Long commandeId;
    private String refCommande;
    private Long userId;
    private String userName;
    private String trackingNumber;
}

