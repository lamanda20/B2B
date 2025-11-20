package com.b2b.dto;

import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
public class PanierDTO {

    private Long id;
    private Long companyId;
    private LocalDate dateCreation;
    private Double total;
    private List<LignePanierDTO> lignes;
}
