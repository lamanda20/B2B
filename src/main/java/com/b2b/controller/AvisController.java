package com.b2b.controller;

import com.b2b.model.Avis;
import com.b2b.model.Company;
import com.b2b.model.Produit;
import com.b2b.repository.CompanyRepository;
import com.b2b.service.AvisService;
import com.b2b.repository.ProduitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/avis")
@CrossOrigin(originPatterns = "*", allowCredentials = "true")
public class AvisController {

    private final AvisService avisService;
    private final ProduitRepository produitRepository;
    private final CompanyRepository companyRepository;

    @Autowired
    public AvisController(AvisService avisService, ProduitRepository produitRepository, CompanyRepository companyRepository) {
        this.avisService = avisService;
        this.produitRepository = produitRepository;
        this.companyRepository = companyRepository;
    }

    @GetMapping
    public ResponseEntity<List<Avis>> findAll() {
        return ResponseEntity.ok(avisService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Avis> findById(@PathVariable Long id) {
        return avisService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/produit/{produitId}")
    public ResponseEntity<List<Avis>> findByProduit(@PathVariable Long produitId) {
        return ResponseEntity.ok(avisService.findByProduit(produitId));
    }

    @GetMapping("/company/{companyId}")
    public ResponseEntity<List<Avis>> findByCompany(@PathVariable Long companyId) {
        return ResponseEntity.ok(avisService.findByCompany(companyId));
    }

    @GetMapping("/produit/{produitId}/moyenne")
    public ResponseEntity<Map<String, Object>> getAverageEvaluation(@PathVariable Long produitId) {
        Double moyenne = avisService.getAverageEvaluation(produitId);
        Map<String, Object> response = new HashMap<>();
        response.put("produitId", produitId);
        response.put("moyenneEvaluation", moyenne != null ? moyenne : 0.0);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<Avis> ajouterAvis(@RequestBody Map<String, Object> avisData) {
        try {
            Long produitId = Long.parseLong(avisData.get("produitId").toString());
            Long companyId = Long.parseLong(avisData.get("companyId").toString());
            String feedback = avisData.get("feedback").toString();
            int note = Integer.parseInt(avisData.get("note").toString());

            Produit produit = produitRepository.findById(produitId)
                    .orElseThrow(() -> new RuntimeException("Produit non trouvé"));
            Company company = companyRepository.findById(companyId)
                    .orElseThrow(() -> new RuntimeException("Company non trouvé"));

            Avis avis = avisService.ajouterAvis(produit, company, feedback, note);
            return ResponseEntity.ok(avis);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimerAvis(@PathVariable Long id) {
        avisService.supprimerAvis(id);
        return ResponseEntity.noContent().build();
    }
}