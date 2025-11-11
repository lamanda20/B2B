package com.b2b.controller;

import com.b2b.model.Commande;
import com.b2b.model.StatutCommande;
import com.b2b.repository.CommandeRepository;
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
@RequestMapping("/commandes")
@CrossOrigin(originPatterns = "*", allowCredentials = "true")
public class CommandeController {

    private final CommandeRepository commandeRepository;

    @Autowired
    public CommandeController(CommandeRepository commandeRepository) {
        this.commandeRepository = commandeRepository;
    }

    /**
     * GET /api/commandes - Liste toutes les commandes
     */
    @GetMapping
    @Transactional(readOnly = true)
    public ResponseEntity<List<Commande>> getAllCommandes() {
        List<Commande> commandes = commandeRepository.findAll();
        // Forcer le chargement des collections lazy
        commandes.forEach(commande -> {
            if (commande.getLignes() != null) {
                commande.getLignes().size(); // Force lazy loading
            }
            if (commande.getClient() != null) {
                commande.getClient().getFullName(); // Force lazy loading
            }
        });
        return ResponseEntity.ok(commandes);
    }

    /**
     * GET /api/commandes/{id} - Détails d'une commande
     */
    @GetMapping("/{id}")
    @Transactional(readOnly = true)
    public ResponseEntity<Commande> getCommandeById(@PathVariable Long id) {
        return commandeRepository.findById(id)
                .map(commande -> {
                    // Forcer le chargement des collections lazy
                    if (commande.getLignes() != null) {
                        commande.getLignes().size();
                    }
                    return ResponseEntity.ok(commande);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * GET /api/commandes/ref/{refCommande} - Rechercher par référence
     */
    @GetMapping("/ref/{refCommande}")
    @Transactional(readOnly = true)
    public ResponseEntity<Commande> getCommandeByRef(@PathVariable String refCommande) {
        return commandeRepository.findByRefCommande(refCommande)
                .map(commande -> {
                    // Forcer le chargement des collections lazy
                    if (commande.getLignes() != null) {
                        commande.getLignes().size();
                    }
                    return ResponseEntity.ok(commande);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * POST /api/commandes - Créer une nouvelle commande
     */
    @PostMapping
    public ResponseEntity<Commande> createCommande(@RequestBody Commande commande) {
        try {
            // Définir le statut par défaut si non fourni
            if (commande.getStatut() == null) {
                commande.setStatut(StatutCommande.EN_ATTENTE);
            }

            Commande saved = commandeRepository.save(commande);
            return ResponseEntity.ok(saved);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * PUT /api/commandes/{id} - Mettre à jour une commande
     */
    @PutMapping("/{id}")
    public ResponseEntity<Commande> updateCommande(@PathVariable Long id, @RequestBody Commande commande) {
        return commandeRepository.findById(id)
                .map(existing -> {
                    if (commande.getStatut() != null) {
                        existing.setStatut(commande.getStatut());
                    }
                    if (commande.getDateCommande() != null) {
                        existing.setDateCommande(commande.getDateCommande());
                    }
                    if (commande.getLivraison() != null) {
                        existing.setLivraison(commande.getLivraison());
                    }
                    Commande updated = commandeRepository.save(existing);
                    return ResponseEntity.ok(updated);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * PATCH /api/commandes/{id}/status - Changer uniquement le statut
     */
    @PatchMapping("/{id}/status")
    public ResponseEntity<Commande> updateCommandeStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> statusData) {
        try {
            String newStatus = statusData.get("status");
            StatutCommande statutEnum = StatutCommande.valueOf(newStatus.toUpperCase());

            return commandeRepository.findById(id)
                    .map(commande -> {
                        commande.setStatut(statutEnum);
                        Commande updated = commandeRepository.save(commande);
                        return ResponseEntity.ok(updated);
                    })
                    .orElse(ResponseEntity.notFound().build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * DELETE /api/commandes/{id} - Supprimer une commande
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCommande(@PathVariable Long id) {
        return commandeRepository.findById(id)
                .map(commande -> {
                    commandeRepository.delete(commande);
                    return ResponseEntity.noContent().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * GET /api/commandes/stats - Statistiques des commandes
     */
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getCommandeStats() {
        List<Commande> commandes = commandeRepository.findAll();

        Map<String, Object> stats = new HashMap<>();
        stats.put("total", commandes.size());
        stats.put("enAttente", commandes.stream().filter(c -> c.getStatut() == StatutCommande.EN_ATTENTE).count());
        stats.put("enPreparation", commandes.stream().filter(c -> c.getStatut() == StatutCommande.EN_PREPARATION).count());
        stats.put("expediee", commandes.stream().filter(c -> c.getStatut() == StatutCommande.EXPEDIEE).count());
        stats.put("livree", commandes.stream().filter(c -> c.getStatut() == StatutCommande.LIVREE).count());
        stats.put("annulee", commandes.stream().filter(c -> c.getStatut() == StatutCommande.ANNULEE).count());

        return ResponseEntity.ok(stats);
    }
}
