package com.b2b.controller;

import com.b2b.model.Produit;
import com.b2b.service.ProduitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/produits")
@CrossOrigin(originPatterns = "*", allowCredentials = "true")
public class ProduitController {

    private final ProduitService produitService;

    @Autowired
    public ProduitController(ProduitService produitService) {
        this.produitService = produitService;
    }

    @GetMapping
    public ResponseEntity<List<Produit>> findAll() {
        return ResponseEntity.ok(produitService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Produit> findById(@PathVariable Long id) {
        return produitService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Produit> create(@RequestBody Produit produit) {
        Produit created = produitService.create(produit);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Produit> update(@PathVariable Long id, @RequestBody Produit produit) {
        try {
            Produit updated = produitService.update(id, produit);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        produitService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/company/{companyId}")
    public ResponseEntity<List<Produit>> findByCompany(@PathVariable Long companyId) {
        return ResponseEntity.ok(produitService.findByCompany(companyId));
    }

    @GetMapping("/categorie/{categorieId}")
    public ResponseEntity<List<Produit>> findByCategorie(@PathVariable Integer categorieId) {
        return ResponseEntity.ok(produitService.findByCategorie(categorieId));
    }

    @GetMapping("/search")
    public ResponseEntity<List<Produit>> searchByName(@RequestParam String name) {
        return ResponseEntity.ok(produitService.searchByName(name));
    }

    @GetMapping("/in-stock")
    public ResponseEntity<List<Produit>> findInStock() {
        return ResponseEntity.ok(produitService.findInStock());
    }
}
