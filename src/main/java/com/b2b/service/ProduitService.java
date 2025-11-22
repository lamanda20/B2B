package com.b2b.service;

import com.b2b.model.Produit;

import java.util.List;
import java.util.Optional;

public interface ProduitService {
    List<Produit> findAll();
    Optional<Produit> findById(Long id);
    Produit create(Produit produit);
    Produit update(Long id, Produit produitDetails);
    void delete(Long id);
    List<Produit> findByCompany(Long companyId);
    List<Produit> findByCategorie(Integer categorieId);
    List<Produit> searchByName(String name);
    List<Produit> findInStock();
}
