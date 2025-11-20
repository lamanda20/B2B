package com.b2b.controller;

import com.b2b.model.Commande;
import com.b2b.model.Panier;
import com.b2b.service.PanierService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/paniers")
@CrossOrigin("*")
public class PanierController {

    private final PanierService panierService;

    public PanierController(PanierService panierService) {
        this.panierService = panierService;
    }

    // Ajouter un produit
    @PostMapping("/{companyId}/add/{produitId}")
    public Panier ajouter(@PathVariable Long companyId,
                          @PathVariable Long produitId,
                          @RequestParam int quantite) {
        return panierService.ajouterProduit(companyId, produitId, quantite);
    }

    // Supprimer un produit
    @DeleteMapping("/{companyId}/delete/{produitId}")
    public Panier supprimer(@PathVariable Long companyId,
                            @PathVariable Long produitId) {
        return panierService.supprimerProduit(companyId, produitId);
    }

    // Valider le panier et créer une commande
    @PostMapping("/{companyId}/valider")
    public Commande valider(@PathVariable Long companyId) {
        return panierService.validerCommande(companyId);
    }
}
