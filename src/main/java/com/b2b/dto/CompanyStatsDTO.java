package com.b2b.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CompanyStatsDTO {
    private Long companyId;
    private String companyName;
    private double total;
}
