package com.b2b.model;

/**
 * Enum représentant les différents statuts d'une livraison
 */
public enum DeliveryStatus {
    PENDING("En attente"),
    IN_PREPARATION("En préparation"),
    IN_TRANSIT("En cours de livraison"),
    SHIPPED("Expédiée"),
    DELIVERED("Livrée"),
    RETURNED("Retournée"),
    CANCELLED("Annulée");

    private final String label;

    DeliveryStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    /**
     * Convertit un StatutCommande en DeliveryStatus
     */
    public static DeliveryStatus fromStatutCommande(StatutCommande statut) {
        switch (statut) {
            case EN_ATTENTE:
            case EN_ATTENTE_PAIEMENT:
                return PENDING;
            case EN_PREPARATION:
                return IN_PREPARATION;
            case EN_COURS:
                return IN_TRANSIT;
            case EXPEDIEE:
                return SHIPPED;
            case LIVREE:
                return DELIVERED;
            case RETOURNEE:
                return RETURNED;
            case ANNULEE:
                return CANCELLED;
            default:
                return PENDING;
        }
    }

    /**
     * Convertit un DeliveryStatus en StatutCommande
     */
    public StatutCommande toStatutCommande() {
        switch (this) {
            case PENDING:
                return StatutCommande.EN_ATTENTE;
            case IN_PREPARATION:
                return StatutCommande.EN_PREPARATION;
            case IN_TRANSIT:
                return StatutCommande.EN_COURS;
            case SHIPPED:
                return StatutCommande.EXPEDIEE;
            case DELIVERED:
                return StatutCommande.LIVREE;
            case RETURNED:
                return StatutCommande.RETOURNEE;
            case CANCELLED:
                return StatutCommande.ANNULEE;
            default:
                return StatutCommande.EN_ATTENTE;
        }
    }
}

