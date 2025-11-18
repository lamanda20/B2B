package com.b2b.controller;

import com.b2b.model.Commande;
import com.b2b.model.StatutCommande;
import com.b2b.service.CommandeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Contrôleur REST pour gérer les commandes
 * Endpoints accessibles via /api/commandes/**
 */
@RestController
@RequestMapping("/api/commandes")
@CrossOrigin(originPatterns = "*", allowCredentials = "true")
public class CommandeController {

    private final CommandeService commandeService;

    @Autowired
    public CommandeController(CommandeService commandeService) {
        this.commandeService = commandeService;
    }

    /**
     * GET /api/commandes - Liste toutes les commandes
     */
    @GetMapping
    public ResponseEntity<List<Commande>> getAllCommandes() {
        return ResponseEntity.ok(commandeService.findAll());
    }

    /**
     * GET /api/commandes/{id} - Détails d'une commande
     */
    @GetMapping("/{id}")
    public ResponseEntity<Commande> getCommandeById(@PathVariable Long id) {
        return commandeService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * GET /api/commandes/ref/{refCommande} - Rechercher par référence
     */
    @GetMapping("/ref/{refCommande}")
    public ResponseEntity<Commande> getCommandeByRef(@PathVariable String refCommande) {
        return commandeService.findByRefCommande(refCommande)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * GET /api/commandes/Company/{CompanyId} - Commandes d'un Company
     */
    @GetMapping("/Company/{CompanyId}")
    public ResponseEntity<List<Commande>> getCommandesByCompany(@PathVariable Long CompanyId) {
        return ResponseEntity.ok(commandeService.findByCompany(CompanyId));
    }

    /**
     * POST /api/commandes - Créer une nouvelle commande
     */
    @PostMapping
    public ResponseEntity<Commande> createCommande(@RequestBody Commande commande) {
        Commande created = commandeService.create(commande);
        return ResponseEntity.ok(created);
    }

    /**
     * POST /api/commandes/{id}/valider - Valider une commande
     */
    @PostMapping("/{id}/valider")
    public ResponseEntity<Map<String, Object>> validerCommande(@PathVariable Long id) {
        try {
            StatutCommande statut = commandeService.validerCommande(id);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Commande validée avec succès");
            response.put("statut", statut);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * PUT /api/commandes/{id}/statut - Mettre à jour le statut
     */
    @PutMapping("/{id}/statut")
    public ResponseEntity<Commande> updateStatut(
            @PathVariable Long id,
            @RequestBody Map<String, String> data) {
        try {
            StatutCommande statut = StatutCommande.valueOf(data.get("statut").toUpperCase());
            Commande updated = commandeService.updateStatut(id, statut);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * GET /api/commandes/{id}/total - Calculer le total d'une commande
     */
    @GetMapping("/{id}/total")
    public ResponseEntity<Map<String, Double>> calculerTotal(@PathVariable Long id) {
        try {
            double total = commandeService.calculerTotal(id);
            Map<String, Double> response = new HashMap<>();
            response.put("total", total);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * DELETE /api/commandes/{id} - Supprimer une commande
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCommande(@PathVariable Long id) {
        commandeService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
