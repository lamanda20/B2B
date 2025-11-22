package com.b2b.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProductStatsDTO {
    private Long productId;
    private String productName;
    private double total;
}
