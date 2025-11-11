package com.b2b.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Table(name = "livraisons")
@Data
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Livraison {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idLivraison;

    private String adresse;
    private String ville;
    private String codePostal;
    private String transporteur;
    private double fraisLivraison;
    private LocalDate dateLivraisonEstimee;

    @OneToOne(mappedBy = "livraison")
    @JsonIgnoreProperties({"livraison", "lignes", "client"})
    private Commande commande;

    /**
     * Définir les informations de livraison et calculer les frais
     * @param ville La ville de destination
     * @return Le montant des frais de livraison en DH
     */
    public double setInfoLivraison(String ville) {
        this.ville = ville;
        this.fraisLivraison = calculerFrais(ville);
        return this.fraisLivraison;
    }

    /**
     * Retourne les informations de livraison
     * @return Les informations de livraison formatées
     */
    public String getInfoLiv() {
        return getInfosSuivi();
    }

    /**
     * Calcule les frais de livraison selon la ville
     * @param ville La ville de destination
     * @return Le montant des frais de livraison en DH
     */
    private double calculerFrais(String ville) {
        if (ville == null || ville.trim().isEmpty()) {
            return 50.0; // Frais par défaut
        }

        String villeLower = ville.toLowerCase();

        switch (villeLower) {
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
     * Retourne les informations de suivi sous forme de chaîne
     * @return Les informations de suivi formatées
     */
    private String getInfosSuivi() {
        StringBuilder info = new StringBuilder();
        info.append("Livraison #").append(idLivraison).append("\n");
        info.append("Adresse: ").append(adresse).append(", ").append(ville).append("\n");
        if (codePostal != null) {
            info.append("Code Postal: ").append(codePostal).append("\n");
        }
        info.append("Transporteur: ").append(transporteur).append("\n");
        info.append("Frais de livraison: ").append(fraisLivraison).append(" DH\n");

        if (dateLivraisonEstimee != null) {
            info.append("Date estimée: ").append(dateLivraisonEstimee).append("\n");
        }

        return info.toString();
    }
}
