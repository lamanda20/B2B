package com.b2b.dto;

import lombok.Data;

@Data
public class CartItemDto {
    private Long productId;
    private String name;       // from Produit
    private String imageUrl;   // from Produit
    private double unitPrice;
    private int quantity;
}
