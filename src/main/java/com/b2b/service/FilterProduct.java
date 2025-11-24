package com.b2b.service;

import com.b2b.model.Filter;
import com.b2b.model.Produit;
import com.b2b.repository.ProduitRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FilterProduct {

    private final ProduitRepository repo;

    public FilterProduct(ProduitRepository repo) {
        this.repo = repo;
    }

    public List<Produit> searchCustom(Filter f) {
        List<Produit> produits = repo.findAll();

        // --- Apply filters and sorting ---
        if (f.getNom() != null && !f.getNom().isEmpty()) {
            produits = produits.stream()
                    .filter(p -> p.getName().toLowerCase().contains(f.getNom().toLowerCase()))
                    .toList();
        }

        if (f.getCategorie() != null && !f.getCategorie().isEmpty()) {
            produits = produits.stream()
                    .filter(p -> p.getCategorie() != null &&
                            p.getCategorie().getName().equalsIgnoreCase(f.getCategorie()))
                    .toList();
        }

        if (f.getPrixMin() != null) {
            produits = produits.stream()
                    .filter(p -> p.getPrice().compareTo(f.getPrixMin()) >= 0)
                    .toList();
        }

        if (f.getPrixMax() != null) {
            produits = produits.stream()
                    .filter(p -> p.getPrice().compareTo(f.getPrixMax()) <= 0)
                    .toList();
        }

        if ("asc".equalsIgnoreCase(f.getTri())) {
            produits = produits.stream()
                    .sorted((p1, p2) -> p1.getPrice().compareTo(p2.getPrice()))
                    .toList();
        } else if ("desc".equalsIgnoreCase(f.getTri())) {
            produits = produits.stream()
                    .sorted((p1, p2) -> p2.getPrice().compareTo(p1.getPrice()))
                    .toList();
        }

        return produits;
    }
}
