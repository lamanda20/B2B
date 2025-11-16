// ...existing code...
package com.b2b.model;

/**
 * Enum minimal pour le statut d'une livraison (wrapper autour de StatutCommande)
 */
public enum DeliveryStatus {
    PENDING("Pending"),
    SHIPPED("Shipped"),
    DELIVERED("Delivered"),
    RETURNED("Returned"),
    CANCELED("Canceled");

    private final String label;

    DeliveryStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public static DeliveryStatus fromStatutCommande(StatutCommande statutCommande) {
        if (statutCommande == null) return PENDING;
        switch (statutCommande) {
            case EXPEDIEE:
                return SHIPPED;
            case LIVREE:
                return DELIVERED;
            case RETOURNEE:
                return RETURNED;
            case ANNULEE:
                return CANCELED;
            default:
                return PENDING;
        }
    }
}
// ...existing code...
