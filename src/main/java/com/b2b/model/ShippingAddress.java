package com.b2b.model;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Classe embeddable représentant une adresse de livraison
 * Peut être réutilisée dans plusieurs entités
 */
@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShippingAddress {

    private String street;          // Rue
    private String city;            // Ville
    private String postalCode;      // Code postal
    private String country;         // Pays (par défaut: Maroc)
    private String phoneNumber;     // Numéro de téléphone
    private String additionalInfo;  // Informations complémentaires (appartement, étage, etc.)

    /**
     * Constructeur simplifié pour le Maroc
     */
    public ShippingAddress(String street, String city, String phoneNumber) {
        this.street = street;
        this.city = city;
        this.phoneNumber = phoneNumber;
        this.country = "Maroc";
    }

    /**
     * Retourne l'adresse complète formatée
     */
    public String getFullAddress() {
        StringBuilder address = new StringBuilder();

        if (street != null && !street.isEmpty()) {
            address.append(street);
        }

        if (additionalInfo != null && !additionalInfo.isEmpty()) {
            address.append(", ").append(additionalInfo);
        }

        if (city != null && !city.isEmpty()) {
            address.append(", ").append(city);
        }

        if (postalCode != null && !postalCode.isEmpty()) {
            address.append(" ").append(postalCode);
        }

        if (country != null && !country.isEmpty()) {
            address.append(", ").append(country);
        }

        return address.toString();
    }

    /**
     * Valide l'adresse (champs obligatoires)
     */
    public boolean isValid() {
        return street != null && !street.trim().isEmpty()
                && city != null && !city.trim().isEmpty()
                && phoneNumber != null && !phoneNumber.trim().isEmpty();
    }
}

