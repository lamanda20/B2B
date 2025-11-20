package com.b2b.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class CommandeDTO {
    private Long id;
    private String refCommande;
    private LocalDate dateCommande;
    private String statut;
    private double total;
}
