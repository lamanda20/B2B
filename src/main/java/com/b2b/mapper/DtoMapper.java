package com.b2b.mapper;

import com.b2b.dto.CategoryDto;
import com.b2b.dto.ProductDto;
import com.b2b.model.Categorie;
import com.b2b.model.Produit;

public class DtoMapper {

    public static CategoryDto toDto(Categorie c) {
        return new CategoryDto(
                c.getIdCat(),
                c.getName(),
                c.getDescription()
        );
    }

    public static ProductDto toDto(Produit p) {
        return new ProductDto(
                p.getId(),
                p.getName(),
                p.getDescription(),
                p.getPrice(),
                p.getStock(),
                p.getImageUrl(),
                p.getCategorie() != null ? p.getCategorie().getIdCat() : null,
                p.getCompany() != null ? p.getCompany().getId() : null,
                p.getFilterTag()      // <-- new
        );
    }

}
