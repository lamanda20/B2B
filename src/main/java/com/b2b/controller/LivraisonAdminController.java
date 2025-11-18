package com.b2b.controller;

import com.b2b.exception.ResourceNotFoundException;
import com.b2b.service.LivraisonService; // Correction de l'import
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/livraison") // URL dédiée à l'admin
public class LivraisonAdminController {

    private final LivraisonService livraisonService;

    @Autowired
    public LivraisonAdminController(LivraisonService livraisonService) {
        this.livraisonService = livraisonService;
    }

    /**
     * Endpoint pour l'admin pour mettre à jour le statut d'une commande.
     * URL: PUT /api/admin/livraison/statut/{commandeId}
     */
    @PutMapping("/statut/{commandeId}")
    public ResponseEntity<Void> updateStatutCommande(
            @PathVariable Long commandeId,
            @RequestBody String nouveauStatut) {

        try {
            // Convertir la chaîne en énumération
            com.b2b.model.StatutCommande statutEnum =
                    com.b2b.model.StatutCommande.valueOf(nouveauStatut.toUpperCase());

            // Appeler votre service
            livraisonService.mettreAJourStatutCommande(commandeId, statutEnum);

            return ResponseEntity.ok().build(); // 200 OK
        } catch (IllegalArgumentException e) {
            // Si le statut envoyé n'existe pas (ex: "ENVOYER" au lieu de "EXPEDIEE")
            throw new ResourceNotFoundException("Statut invalide: " + nouveauStatut);
        }
    }
}