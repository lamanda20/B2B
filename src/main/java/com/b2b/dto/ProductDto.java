package com.b2b.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {

    private Long id;
    private String name;
    private String description;

    private double price;
    private int stock;

    private String imageUrl;

    private Integer categoryId;
    private Long companyId;

    private String filterTag;

}
