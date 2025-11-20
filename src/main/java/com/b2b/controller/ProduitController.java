package com.b2b.controller;

import com.b2b.dto.ProduitDTO;
import com.b2b.dto.ProduitSearchDTO;
import com.b2b.service.ProduitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/produits")
@CrossOrigin(origins = "*")
public class ProduitController {

    @Autowired
    private ProduitService produitService;

    // GET /api/produits - Récupère tous les produits ou recherche par mot-clé
    @GetMapping
    public ResponseEntity<List<ProduitDTO>> getAllProduits(
            @RequestParam(required = false) String q) {
        List<ProduitDTO> produits = produitService.findAll(q);
        return ResponseEntity.ok(produits);
    }

    // GET /api/produits/{id} - Récupère un produit par son ID
    @GetMapping("/{id}")
    public ResponseEntity<ProduitDTO> getProduitById(@PathVariable Long id) {
        ProduitDTO produit = produitService.findById(id);
        return ResponseEntity.ok(produit);
    }

    // POST /api/produits/search - Recherche avancée avec filtres
    @PostMapping("/search")
    public ResponseEntity<List<ProduitDTO>> searchProduits(@RequestBody ProduitSearchDTO searchDTO) {
        List<ProduitDTO> produits = produitService.searchProduits(searchDTO);
        return ResponseEntity.ok(produits);
    }

    // GET /api/produits/categorie/{categorieId} - Filtre par catégorie
    @GetMapping("/categorie/{categorieId}")
    public ResponseEntity<List<ProduitDTO>> getProduitsByCategorie(@PathVariable Long categorieId) {
        List<ProduitDTO> produits = produitService.findByCategorie(categorieId);
        return ResponseEntity.ok(produits);
    }

    // GET /api/produits/price-range - Filtre par fourchette de prix
    @GetMapping("/price-range")
    public ResponseEntity<List<ProduitDTO>> getProduitsByPriceRange(
            @RequestParam BigDecimal minPrice,
            @RequestParam BigDecimal maxPrice) {
        List<ProduitDTO> produits = produitService.findByPriceRange(minPrice, maxPrice);
        return ResponseEntity.ok(produits);
    }

    // GET /api/produits/search/keyword - Recherche par mot-clé (alternative)
    @GetMapping("/search/keyword")
    public ResponseEntity<List<ProduitDTO>> searchByKeyword(@RequestParam String keyword) {
        List<ProduitDTO> produits = produitService.findAll(keyword);
        return ResponseEntity.ok(produits);
    }
}