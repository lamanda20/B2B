package com.b2b.dto;

import lombok.Data;
import java.util.List;

@Data
public class CartDto {
    private Long companyId;
    private List<CartItemDto> items;
    private double subtotal;
}
