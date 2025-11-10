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
    private String telephone;
    private String transporteur;
    private double fraisLivraison;
    private LocalDate dateEnvoi;
    private LocalDate dateEstimee;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnoreProperties({"livraisons", "commandes"})
    private User user;

    @OneToOne(mappedBy = "livraison")
    @JsonIgnoreProperties({"livraison", "lignes", "user"})
    private Commande commande;

    /**
     * Calcule les frais de livraison selon la ville
     * @param ville La ville de destination
     * @return Le montant des frais de livraison en DH
     */
    public double calculerFrais(String ville) {
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
    public String getInfosSuivi() {
        StringBuilder info = new StringBuilder();
        info.append("Livraison #").append(idLivraison).append("\n");
        info.append("Adresse: ").append(adresse).append(", ").append(ville).append("\n");
        info.append("Téléphone: ").append(telephone).append("\n");
        info.append("Transporteur: ").append(transporteur).append("\n");
        info.append("Frais de livraison: ").append(fraisLivraison).append(" DH\n");

        if (dateEnvoi != null) {
            info.append("Date d'envoi: ").append(dateEnvoi).append("\n");
        } else {
            info.append("Date d'envoi: Non encore expédiée\n");
        }

        if (dateEstimee != null) {
            info.append("Date estimée: ").append(dateEstimee).append("\n");
        }

        return info.toString();
    }
}
