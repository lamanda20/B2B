package com.b2b.controller;

import com.b2b.dto.SuiviDTO;
import com.b2b.exception.ResourceNotFoundException;
import com.b2b.model.Commande;
import com.b2b.model.Livraison;
import com.b2b.repository.CommandeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/suivi") // URL dédiée au suivi client
public class LivraisonSuiviController {

    private final CommandeRepository commandeRepository;

    @Autowired
    public LivraisonSuiviController(CommandeRepository commandeRepository) {
        this.commandeRepository = commandeRepository;
    }

    /**
     * Endpoint pour le client final, pour suivre sa commande par sa référence.
     * URL: GET /api/suivi/{refCommande}
     */
    @GetMapping("/{refCommande}")
    public ResponseEntity<SuiviDTO> getSuiviParReference(@PathVariable String refCommande) {

        // 1. Trouver la commande
        Commande commande = commandeRepository.findByRefCommande(refCommande)
                .orElseThrow(() -> new ResourceNotFoundException("Commande non trouvée: " + refCommande));

        // 2. Vérifier la livraison
        Livraison livraison = commande.getLivraison();
        if (livraison == null) {
            throw new ResourceNotFoundException("Livraison non encore initiée pour la commande: " + refCommande);
        }

        // 3. Créer le DTO de réponse
        SuiviDTO dto = new SuiviDTO(
                commande.getRefCommande(),
                commande.getStatut(),
                livraison.getTransporteur(),
                livraison.getDateEnvoi(),
                livraison.getDateEstimee()
        );

        // 4. Renvoyer le DTO avec un statut 200 OK
        return ResponseEntity.ok(dto);
    }
}