package com.b2b.dto;

import com.b2b.model.StatutCommande;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SuiviDTO {
    private String refCommande;
    private StatutCommande statut;
    private String transporteur;
    private LocalDate dateEnvoi;
    private LocalDate dateEstimee;
}

