package com.b2b.controller;

import com.b2b.dto.CategoryStatsDTO;
import com.b2b.dto.CompanyStatsDTO;
import com.b2b.dto.ProductStatsDTO;
import com.b2b.service.StatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stats")
@CrossOrigin(originPatterns = "*", allowCredentials = "true")
public class StatsController {

    private final StatsService statsService;

    @Autowired
    public StatsController(StatsService statsService) {
        this.statsService = statsService;
    }

    // Best Companies Acheteuses (tous)
    @GetMapping("/companies/acheteuses")
    public ResponseEntity<List<CompanyStatsDTO>> bestCompaniesAcheteuses() {
        return ResponseEntity.ok(statsService.bestCompaniesAcheteuses());
    }

    // TOP 3 Companies Acheteuses
    @GetMapping("/companies/acheteuses/top3")
    public ResponseEntity<List<CompanyStatsDTO>> top3CompaniesAcheteuses() {
        return ResponseEntity.ok(statsService.top3CompaniesAcheteuses());
    }

    // Best Companies Vendeuses (tous)
    @GetMapping("/companies/vendeuses")
    public ResponseEntity<List<CompanyStatsDTO>> bestCompaniesVendeuses() {
        return ResponseEntity.ok(statsService.bestCompaniesVendeuses());
    }

    // TOP 3 Companies Vendeuses
    @GetMapping("/companies/vendeuses/top3")
    public ResponseEntity<List<CompanyStatsDTO>> top3CompaniesVendeuses() {
        return ResponseEntity.ok(statsService.top3CompaniesVendeuses());
    }

    // Best Products (tous)
    @GetMapping("/products")
    public ResponseEntity<List<ProductStatsDTO>> bestProducts() {
        return ResponseEntity.ok(statsService.bestProducts());
    }

    // TOP 3 Products
    @GetMapping("/products/top3")
    public ResponseEntity<List<ProductStatsDTO>> top3Products() {
        return ResponseEntity.ok(statsService.top3Products());
    }

    // Best Categories (toutes)
    @GetMapping("/categories")
    public ResponseEntity<List<CategoryStatsDTO>> bestCategories() {
        return ResponseEntity.ok(statsService.bestCategories());
    }

    // TOP 3 Categories
    @GetMapping("/categories/top3")
    public ResponseEntity<List<CategoryStatsDTO>> top3Categories() {
        return ResponseEntity.ok(statsService.top3Categories());
    }

    // Nombre Companies Actives
    @GetMapping("/companies/actives/nbr")
    public ResponseEntity<Integer> nombreCompaniesActives() {
        return ResponseEntity.ok(statsService.nombreCompaniesActives());
    }

    // Nombre produits
    @GetMapping("/products/nbr")
    public ResponseEntity<Integer> nombreProduits() {
        return ResponseEntity.ok(statsService.nombreProduits());
    }

    // Nombre commandes passees
    @GetMapping("/commandes/nbr")
    public ResponseEntity<Integer> nombreCommadesPasser() {
        return ResponseEntity.ok(statsService.nombreCommadesPasser());
    }
}
