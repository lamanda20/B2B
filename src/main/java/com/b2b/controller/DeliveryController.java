package com.b2b.controller;

import com.b2b.exception.ResourceNotFoundException;
import com.b2b.model.Livraison;
import com.b2b.repository.LivraisonRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/deliveries")
@CrossOrigin(originPatterns = "*", allowCredentials = "true")
public class DeliveryController {

    private final LivraisonRepository livraisonRepository;

    public DeliveryController(LivraisonRepository livraisonRepository) {
        this.livraisonRepository = livraisonRepository;
    }



    @GetMapping("/calculate-shipping")
    public ResponseEntity<Map<String, Object>> calculateShipping(@RequestParam(required = false) String city,
                                                                   @RequestParam(required = false) String ville) {
        String targetCity = (city != null && !city.isBlank()) ? city : ville;
        if (targetCity == null || targetCity.isBlank()) {
            Map<String, Object> err = new HashMap<>();
            err.put("message", "Parameter 'city' or 'ville' is required");
            err.put("status", 400);
            return ResponseEntity.badRequest().body(err);
        }
        Livraison temp = new Livraison();
        temp.calculerFrais(targetCity);
        Map<String, Object> response = new HashMap<>();
        response.put("city", targetCity);
        response.put("frais", temp.getFraisLivraison());
        return ResponseEntity.ok(response);
    }

    /**
     * POST /api/deliveries - create a new Livraison (accept JSON body)
     */
    @PostMapping
    public ResponseEntity<Livraison> createDelivery(@RequestBody Livraison livraison) {
        if (livraison.getVille() != null) {
            livraison.calculerFrais(livraison.getVille());
        }
        Livraison saved = livraisonRepository.save(livraison);
        return ResponseEntity.ok(saved);
    }

    /**
     * PUT /api/deliveries/{id}/transporteur - Met à jour le transporteur (texte brut)
     */

    @PutMapping("/{id}/transporteur")
    public ResponseEntity<Void> updateTransporteur(@PathVariable Long id, @RequestBody String transporteur) {
        Livraison livraison = livraisonRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Livraison non trouvée: " + id));

        livraison.setTransporteur(transporteur);
        livraisonRepository.save(livraison);

        return ResponseEntity.ok().build();
    }
}
