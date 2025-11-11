package com.b2b.service.impl;

import com.b2b.model.AppUser;
import com.b2b.model.Commande;
import com.b2b.model.Livraison;
import com.b2b.model.StatutCommande;
import com.b2b.repository.CommandeRepository;
import com.b2b.repository.LivraisonRepository;
import com.b2b.service.LivraisonService;
// Importez le service de notification de la Personne 7
// import com.b2b.service.NotificationService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@Transactional // Assure que les opérations de base de données sont cohérentes
public class LivraisonServiceImpl implements LivraisonService {

    private final LivraisonRepository livraisonRepository;
    private final CommandeRepository commandeRepository;
    // Injectez le service de la Personne 7 quand il sera prêt
    // private final NotificationService notificationService;

    // Injection des dépendances par le constructeur
    @Autowired
    public LivraisonServiceImpl(LivraisonRepository livraisonRepository,
                                CommandeRepository commandeRepository) {
        // NotificationService notificationService) {
        this.livraisonRepository = livraisonRepository;
        this.commandeRepository = commandeRepository;
        // this.notificationService = notificationService;
    }

    @Override
    public Livraison creerLivraisonPourCommande(Commande commande) {
        if (commande.getClient() == null) {
            throw new IllegalArgumentException("La commande doit être associée à un client.");
        }

        // Récupérer les infos du client pour créer la livraison
        var client = commande.getClient();

        Livraison livraison = new Livraison();
        // Note: AppUser n'a pas d'adresse directement, il faudra l'obtenir autrement
        // Pour l'instant, on utilise des valeurs par défaut
        livraison.setAdresse("Adresse à définir");
        livraison.setVille("Ville à définir");
        livraison.setCodePostal("00000");

        // Utiliser la méthode setInfoLivraison de l'entité Livraison
        livraison.setInfoLivraison("Casablanca");

        // Transporteur par défaut
        livraison.setTransporteur("Maroc Poste");
        livraison.setDateLivraisonEstimee(LocalDate.now().plusDays(3));

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

        // Mettre à jour les informations de livraison selon le statut
        Livraison livraison = commande.getLivraison();
        if (livraison != null) {
            switch (nouveauStatut) {
                case EXPEDIEE:
                case EN_COURS:
                    livraison.setDateLivraisonEstimee(LocalDate.now().plusDays(3)); // Estimation de 3 jours
                    break;
                case LIVREE:
                    // La livraison est terminée
                    livraison.setDateLivraisonEstimee(LocalDate.now());
                    break;
                case RETOURNEE:
                    // La commande a été retournée
                    break;
                default:
                    break;
            }
            livraisonRepository.save(livraison);
        }

        // Sauvegarder la commande mise à jour
        Commande commandeSauvegardee = commandeRepository.save(commande);

        // Appeler le service de notification (Personne 7)
        // String message = "Votre commande #" + commande.getRefCommande() + " est maintenant : " + nouveauStatut;
        // notificationService.envoyerEmail(commande.getClient(), "Suivi de commande", message);

        return commandeSauvegardee;
    }
}