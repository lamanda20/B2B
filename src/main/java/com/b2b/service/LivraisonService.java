package com.b2b.service;

import com.b2b.model.Commande;
import com.b2b.model.Livraison;
import com.b2b.model.StatutCommande;

public interface LivraisonService {

    /**
     * Crée une entité Livraison, calcule les frais, et l'associe à une Commande.
     * @param commande La Commande qui vient d'être validée.
     * @return L'objet Livraison qui a été créé et sauvegardé.
     */
    Livraison creerLivraisonPourCommande(Commande commande);

    /**
     * Met à jour le statut d'une commande (ex: EXPEDIEE, LIVREE) et notifie le Company.
     * @param commandeId L'ID de la commande à mettre à jour.
     * @param nouveauStatut Le nouveau statut à appliquer.
     * @return La Commande mise à jour.
     */
    Commande mettreAJourStatutCommande(Long commandeId, StatutCommande nouveauStatut);
}

