package com.b2b.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CategoryStatsDTO {
    private Integer categoryId;
    private String categoryName;
    private double total;
}
