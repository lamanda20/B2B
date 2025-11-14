package com.b2b.service;

import com.b2b.dto.DeliveryDTO;
import com.b2b.model.*;
import com.b2b.repository.CommandeRepository;
import com.b2b.repository.DeliveryRepository;
import com.b2b.repository.LivraisonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service pour gérer les livraisons
 * Couche métier entre le contrôleur et le repository
 */
@Service
@Transactional
public class DeliveryService {

    private final DeliveryRepository deliveryRepository;
    private final LivraisonRepository livraisonRepository;
    private final CommandeRepository commandeRepository;

    @Autowired
    public DeliveryService(DeliveryRepository deliveryRepository,
                          LivraisonRepository livraisonRepository,
                          CommandeRepository commandeRepository) {
        this.deliveryRepository = deliveryRepository;
        this.livraisonRepository = livraisonRepository;
        this.commandeRepository = commandeRepository;
    }

    /**
     * Récupère toutes les livraisons
     */
    public List<DeliveryDTO> getAllDeliveries() {
        // Utilise le repository Livraison existant et convertit en DTO
        return livraisonRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Récupère une livraison par ID
     */
    public Optional<DeliveryDTO> getDeliveryById(Long id) {
        return livraisonRepository.findById(id)
                .map(this::convertToDTO);
    }

    /**
     * Récupère une livraison par ID de commande
     */
    public Optional<DeliveryDTO> getDeliveryByOrderId(Long orderId) {
        return livraisonRepository.findByCommandeId(orderId)
                .map(this::convertToDTO);
    }

    /**
     * Récupère les livraisons par statut
     */
    public List<DeliveryDTO> getDeliveriesByStatus(StatutCommande statut) {
        return livraisonRepository.findByStatut(statut).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Récupère les livraisons par ville
     */
    public List<DeliveryDTO> getDeliveriesByCity(String city) {
        return livraisonRepository.findByVille(city).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Récupère les livraisons d'un utilisateur
     */
    public List<DeliveryDTO> getDeliveriesByUserId(Long userId) {
        return livraisonRepository.findByUserId(userId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Crée une nouvelle livraison pour une commande
     */
    public DeliveryDTO createDeliveryForOrder(Long orderId, String carrier) {
        Commande commande = commandeRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Commande introuvable avec l'ID: " + orderId));

        if (commande.getUser() == null) {
            throw new IllegalArgumentException("La commande doit être associée à un utilisateur.");
        }

        User user = commande.getUser();

        // Créer la livraison
        Livraison livraison = new Livraison();
        livraison.setAdresse(user.getAdresse());
        livraison.setVille(user.getVille());
        livraison.setTelephone(user.getTelephone());
        livraison.setTransporteur(carrier != null ? carrier : "Maroc Poste");

        // Calculer les frais
        livraison.setFraisLivraison(livraison.calculerFrais(user.getVille()));

        // Associer l'utilisateur et la commande
        livraison.setUser(user);
        livraison.setCommande(commande);

        // Sauvegarder
        Livraison saved = livraisonRepository.save(livraison);

        // Mettre à jour la commande
        commande.setLivraison(saved);
        commandeRepository.save(commande);

        return convertToDTO(saved);
    }

    /**
     * Met à jour le statut d'une livraison
     */
    public DeliveryDTO updateDeliveryStatus(Long deliveryId, StatutCommande newStatus) {
        Livraison livraison = livraisonRepository.findById(deliveryId)
                .orElseThrow(() -> new RuntimeException("Livraison introuvable avec l'ID: " + deliveryId));

        if (livraison.getCommande() == null) {
            throw new IllegalStateException("La livraison n'est pas associée à une commande.");
        }

        // Mettre à jour le statut de la commande
        Commande commande = livraison.getCommande();
        commande.setStatut(newStatus);

        // Mettre à jour les dates selon le statut
        switch (newStatus) {
            case EXPEDIEE:
            case EN_COURS:
                livraison.setDateEnvoi(LocalDate.now());
                livraison.setDateEstimee(LocalDate.now().plusDays(3)); // Estimation de 3 jours
                break;
            case LIVREE:
                if (livraison.getDateEnvoi() == null) {
                    livraison.setDateEnvoi(LocalDate.now());
                }
                break;
            case RETOURNEE:
            case ANNULEE:
                // Pas de modification de dates
                break;
        }

        // Sauvegarder
        commandeRepository.save(commande);
        Livraison updated = livraisonRepository.save(livraison);

        return convertToDTO(updated);
    }

    /**
     * Calcule les frais de livraison pour une ville donnée
     */
    public double calculateShippingCost(String city) {
        Livraison temp = new Livraison();
        return temp.calculerFrais(city);
    }

    /**
     * Recherche une livraison par numéro de tracking
     * (Pour l'instant, le tracking est basé sur l'ID)
     */
    public Optional<DeliveryDTO> trackDelivery(String trackingNumber) {
        // Le format est TRK-{id}
        if (trackingNumber.startsWith("TRK-")) {
            try {
                Long id = Long.parseLong(trackingNumber.substring(4));
                return getDeliveryById(id);
            } catch (NumberFormatException e) {
                return Optional.empty();
            }
        }
        return Optional.empty();
    }

    /**
     * Supprime une livraison
     */
    public void deleteDelivery(Long id) {
        Livraison livraison = livraisonRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Livraison introuvable avec l'ID: " + id));

        // Dissocier de la commande si nécessaire
        if (livraison.getCommande() != null) {
            Commande commande = livraison.getCommande();
            commande.setLivraison(null);
            commandeRepository.save(commande);
        }

        livraisonRepository.delete(livraison);
    }

    /**
     * Convertit une entité Livraison en DeliveryDTO
     */
    private DeliveryDTO convertToDTO(Livraison livraison) {
        DeliveryDTO dto = new DeliveryDTO();
        dto.setId(livraison.getIdLivraison());
        dto.setAdresse(livraison.getAdresse());
        dto.setVille(livraison.getVille());
        dto.setTelephone(livraison.getTelephone());
        dto.setTransporteur(livraison.getTransporteur());
        dto.setFraisLivraison(livraison.getFraisLivraison());
        dto.setDateEnvoi(livraison.getDateEnvoi());
        dto.setDateEstimee(livraison.getDateEstimee());
        dto.setTrackingNumber("TRK-" + livraison.getIdLivraison());

        // Informations de la commande
        if (livraison.getCommande() != null) {
            dto.setCommandeId(livraison.getCommande().getId());
            dto.setRefCommande(livraison.getCommande().getRefCommande());
            dto.setStatut(livraison.getCommande().getStatut());
        }

        // Informations de l'utilisateur
        if (livraison.getUser() != null) {
            dto.setUserId(livraison.getUser().getId());
            dto.setUserName(livraison.getUser().getNom());
        }

        return dto;
    }

    /**
     * Convertit un DeliveryDTO en entité Livraison
     */
    private Livraison convertToEntity(DeliveryDTO dto) {
        Livraison livraison = new Livraison();
        livraison.setIdLivraison(dto.getId());
        livraison.setAdresse(dto.getAdresse());
        livraison.setVille(dto.getVille());
        livraison.setTelephone(dto.getTelephone());
        livraison.setTransporteur(dto.getTransporteur());
        livraison.setFraisLivraison(dto.getFraisLivraison());
        livraison.setDateEnvoi(dto.getDateEnvoi());
        livraison.setDateEstimee(dto.getDateEstimee());
        return livraison;
    }
}

