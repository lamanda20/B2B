package com.b2b.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public class ProduitUpdateDTO {
    @NotBlank public String name;
    public String description;
    @NotNull @DecimalMin("0.0") public BigDecimal price;
    @NotNull @Min(0) public Integer stock;
    public Long sellerId;
}
