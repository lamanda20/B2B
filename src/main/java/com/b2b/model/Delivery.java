package com.b2b.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Entité Delivery - Version anglaise de Livraison
 * Wrapper autour de l'entité Livraison existante pour une API cohérente
 */
@Entity
@Table(name = "deliveries")
@Data
@NoArgsConstructor
public class Delivery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private ShippingAddress shippingAddress;

    private String carrier;                 // Transporteur (Maroc Poste, Jumia Express, etc.)
    private Double shippingCost;            // Frais de livraison en DH
    private LocalDate shippingDate;         // Date d'envoi
    private LocalDate estimatedDeliveryDate; // Date estimée de livraison
    private String trackingNumber;          // Numéro de suivi

    @Enumerated(EnumType.STRING)
    private DeliveryStatus status;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private AdminUser user;

    @OneToOne
    @JoinColumn(name = "order_id")
    private Commande order;

    /**
     * Constructeur à partir d'une Livraison existante
     */
    public Delivery(Livraison livraison) {
        if (livraison != null) {
            this.id = livraison.getIdLivraison();

            // Créer l'adresse
            this.shippingAddress = new ShippingAddress(
                    livraison.getAdresse(),
                    livraison.getVille(),
                    livraison.getTelephone()
            );

            this.carrier = livraison.getTransporteur();
            this.shippingCost = livraison.getFraisLivraison();
            this.shippingDate = livraison.getDateEnvoi();
            this.estimatedDeliveryDate = livraison.getDateEstimee();
            this.trackingNumber = "TRK-" + livraison.getIdLivraison();
            // Livraison model n'a pas de getUser(), on ne peut pas mapper directement
            this.user = null;
            this.order = livraison.getCommande();

            // Convertir le statut
            if (livraison.getCommande() != null && livraison.getCommande().getStatut() != null) {
                this.status = DeliveryStatus.fromStatutCommande(livraison.getCommande().getStatut());
            } else {
                this.status = DeliveryStatus.PENDING;
            }
        }
    }

    /**
     * Convertit cette Delivery en Livraison
     */
    public Livraison toLivraison() {
        Livraison livraison = new Livraison();
        livraison.setIdLivraison(this.id);

        if (this.shippingAddress != null) {
            livraison.setAdresse(this.shippingAddress.getStreet());
            livraison.setVille(this.shippingAddress.getCity());
            livraison.setTelephone(this.shippingAddress.getPhoneNumber());
        }

        livraison.setTransporteur(this.carrier);
        livraison.setFraisLivraison(this.shippingCost != null ? this.shippingCost : 0.0);
        livraison.setDateEnvoi(this.shippingDate);
        livraison.setDateEstimee(this.estimatedDeliveryDate);
        // Ne pas appeler livraison.setUser(...) car la classe Livraison n'expose pas ce setter
        livraison.setCommande(this.order);

        return livraison;
    }

    /**
     * Calcule les frais de livraison selon la ville
     */
    public double calculateShippingCost() {
        if (shippingAddress == null || shippingAddress.getCity() == null) {
            return 50.0; // Frais par défaut
        }

        String city = shippingAddress.getCity().toLowerCase();

        switch (city) {
            case "casablanca":
                return 20.0;
            case "rabat":
            case "marrakech":
            case "tanger":
                return 35.0;
            default:
                return 50.0; // Reste du Maroc
        }
    }

    /**
     * Génère automatiquement le numéro de tracking
     */
    @PrePersist
    @PreUpdate
    public void generateTrackingNumber() {
        if (this.trackingNumber == null && this.id != null) {
            this.trackingNumber = "TRK-" + this.id;
        }
    }

    /**
     * Retourne les informations de suivi formatées
     */
    public String getTrackingInfo() {
        StringBuilder info = new StringBuilder();
        info.append("Delivery #").append(id).append("\n");
        info.append("Tracking: ").append(trackingNumber).append("\n");

        if (shippingAddress != null) {
            info.append("Address: ").append(shippingAddress.getFullAddress()).append("\n");
        }

        info.append("Carrier: ").append(carrier).append("\n");
        info.append("Shipping Cost: ").append(shippingCost).append(" DH\n");
        info.append("Status: ").append(status.getLabel()).append("\n");

        if (shippingDate != null) {
            info.append("Shipping Date: ").append(shippingDate).append("\n");
        } else {
            info.append("Shipping Date: Not shipped yet\n");
        }

        if (estimatedDeliveryDate != null) {
            info.append("Estimated Delivery: ").append(estimatedDeliveryDate).append("\n");
        }

        return info.toString();
    }
}
