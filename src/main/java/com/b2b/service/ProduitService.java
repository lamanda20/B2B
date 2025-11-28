package com.b2b.service;

import com.b2b.dto.ProductDto;
import com.b2b.model.Produit;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ProduitService {

    List<Produit> findAll();
    Optional<Produit> findById(Long id);

    Produit create(ProductDto dto);
    Produit update(Long id, ProductDto dto);

    void delete(Long id);

    List<Produit> findByCompany(Long companyId);
    List<Produit> findByCategorie(Integer categorieId);

    List<Produit> searchByName(String name);
    List<Produit> findInStock();

    // ⭐ REQUIRED for /api/products/filter
    List<Produit> filter(int categoryId, Map<String, String> filters);
}
