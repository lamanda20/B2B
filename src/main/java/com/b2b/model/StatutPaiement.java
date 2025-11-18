package com.b2b.model;

public enum StatutPaiement {
    EN_ATTENTE("En attente"),
    VALIDE("Validé"),
    REFUSE("Refusé"),
    REMBOURSE("Remboursé"),
    ANNULE("Annulé");

    private final String label;

    StatutPaiement(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
