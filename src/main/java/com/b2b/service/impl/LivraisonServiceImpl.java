package com.b2b.service.impl;

import com.b2b.model.Company;
import com.b2b.model.Commande;
import com.b2b.model.Livraison;
import com.b2b.model.StatutCommande;
import com.b2b.repository.CommandeRepository;
import com.b2b.repository.LivraisonRepository;
import com.b2b.service.LivraisonService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@Transactional
public class LivraisonServiceImpl implements LivraisonService {

    private final LivraisonRepository livraisonRepository;
    private final CommandeRepository commandeRepository;

    @Autowired
    public LivraisonServiceImpl(LivraisonRepository livraisonRepository,
                                CommandeRepository commandeRepository) {
        this.livraisonRepository = livraisonRepository;
        this.commandeRepository = commandeRepository;
    }

    @Override
    public Livraison creerLivraisonPourCommande(Commande commande) {
        if (commande.getCompany() == null) {
            throw new IllegalArgumentException("La commande doit être associée à une company.");
        }

        // Récupérer les infos de la company pour créer la livraison
        var company = commande.getCompany();

        Livraison livraison = new Livraison();
        livraison.setAdresse("Adresse à définir");
        livraison.setVille("Casablanca");
        livraison.setCodePostal("00000");

        // Calculer les frais de livraison
        livraison.calculerFrais("Casablanca");

        livraison.setTransporteur("Maroc Poste");
        livraison.setDateEstimee(LocalDate.now().plusDays(3));

        // Sauvegarder la livraison
        Livraison saved = livraisonRepository.save(livraison);

        // Associer la livraison à la commande et sauvegarder la commande
        commande.setLivraison(saved);
        commandeRepository.save(commande);

        return saved;
    }

    @Override
    public Commande mettreAJourStatutCommande(Long commandeId, StatutCommande nouveauStatut) {
        Commande commande = commandeRepository.findById(commandeId)
                .orElseThrow(() -> new EntityNotFoundException("Commande non trouvée avec l'ID: " + commandeId));

        commande.setStatut(nouveauStatut);

        Livraison livraison = commande.getLivraison();
        if (livraison != null) {
            switch (nouveauStatut) {
                case EXPEDIEE:
                case EN_COURS:
                    livraison.setDateEstimee(LocalDate.now().plusDays(3));
                    break;
                case LIVREE:
                    livraison.setDateEstimee(LocalDate.now());
                    break;
                case RETOURNEE:
                    break;
                default:
                    break;
            }
            livraisonRepository.save(livraison);
        }

        Commande commandeSauvegardee = commandeRepository.save(commande);

        return commandeSauvegardee;
    }
}