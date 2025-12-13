package com.b2b.mapper;

import com.b2b.dto.ProductDto;
import com.b2b.model.Produit;

public class ProductMapper {

    public static ProductDto toDto(Produit p) {
        if (p == null) return null;

        return new ProductDto(
                p.getId(),
                p.getName(),
                p.getDescription(),
                p.getPrice(),
                p.getStock(),
                p.getImageUrl(),
                p.getCategorie() != null ? p.getCategorie().getIdCat() : null,
                p.getCompany() != null ? p.getCompany().getId() : null,
                p.getFilterTag()
        );
    }

    public static Produit toEntity(ProductDto dto) {
        if (dto == null) return null;

        Produit p = new Produit();
        p.setId(dto.getId());
        p.setName(dto.getName());
        p.setDescription(dto.getDescription());
        p.setPrice(dto.getPrice());
        p.setStock(dto.getStock());
        p.setImageUrl(dto.getImageUrl());
        p.setFilterTag(dto.getFilterTag());

        return p;
    }
}
