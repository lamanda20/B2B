package com.b2b.model;

/**
 * Enum pour les statuts de paiement
 */
public enum PaymentStatus {
    EN_ATTENTE("En attente"),
    VALIDÉ("Validé"),
    REFUSÉ("Refusé"),
    REMBOURSÉ("Remboursé");

    private final String label;

    PaymentStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}