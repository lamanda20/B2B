package com.b2b.controller;

import com.b2b.model.Livraison;
import com.b2b.repository.LivraisonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * Contrôleur REST pour gérer les livraisons
 * Endpoints accessibles via /api/livraisons/**
 */
@RestController
@RequestMapping("/api/livraisons")
@CrossOrigin(originPatterns = "*", allowCredentials = "true")
public class LivraisonController {

    private final LivraisonRepository livraisonRepository;

    @Autowired
    public LivraisonController(LivraisonRepository livraisonRepository) {
        this.livraisonRepository = livraisonRepository;
    }

    /**
     * GET /api/livraisons - Liste toutes les livraisons
     */
    @GetMapping
    public ResponseEntity<List<Livraison>> getAllLivraisons() {
        return ResponseEntity.ok(livraisonRepository.findAll());
    }

    /**
     * GET /api/livraisons/{id} - Détails d'une livraison
     */
    @GetMapping("/{id}")
    public ResponseEntity<Livraison> getLivraisonById(@PathVariable Long id) {
        return livraisonRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * POST /api/livraisons - Créer une nouvelle livraison
     */
    @PostMapping
    public ResponseEntity<Livraison> createLivraison(@RequestBody Livraison livraison) {
        if (livraison.getVille() != null) {
            livraison.calculerFrais(livraison.getVille());
        }
        return ResponseEntity.ok(livraisonRepository.save(livraison));
    }

    /**
     * GET /api/livraisons/calculate-frais?ville=XXX - Calculer frais de livraison
     */
    @GetMapping("/calculate-frais")
    public ResponseEntity<Map<String, Object>> calculateFrais(@RequestParam(required = false) String ville,
                                                               @RequestParam(required = false) String city) {
        String targetVille = (ville != null && !ville.isBlank()) ? ville : city;
        if (targetVille == null || targetVille.isBlank()) {
            Map<String, Object> err = new HashMap<>();
            err.put("message", "Parameter 'ville' or 'city' is required");
            err.put("status", 400);
            return ResponseEntity.badRequest().body(err);
        }
        Livraison temp = new Livraison();
        temp.calculerFrais(targetVille);
        Map<String, Object> response = new HashMap<>();
        response.put("ville", targetVille);
        response.put("frais", temp.getFraisLivraison());
        return ResponseEntity.ok(response);
    }
}
