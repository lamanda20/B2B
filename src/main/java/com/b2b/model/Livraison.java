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
    private String telephone;
    private String transporteur;
    private double fraisLivraison;
    private LocalDate dateEnvoi;
    private LocalDate dateEstimee;

    @OneToOne(mappedBy = "livraison")
    @JsonIgnoreProperties({"livraison", "lignes", "Company", "paiements"})
    private Commande commande;

    @PrePersist
    protected void onCreate() {
        if (dateEnvoi == null) {
            dateEnvoi = LocalDate.now();
        }
    }

    // Méthode calculerFrais - Retourne String selon le diagramme
    public String calculerFrais(String ville) {
        if (ville == null || ville.trim().isEmpty()) {
            this.fraisLivraison = 50.0;
            return "Frais de livraison: 50.0 DH (ville non spécifiée)";
        }

        String villeLower = ville.toLowerCase();
        switch (villeLower) {
            case "casablanca", "rabat":
                this.fraisLivraison = 20.0;
                break;
            case "marrakech", "fès", "tanger", "agadir":
                this.fraisLivraison = 35.0;
                break;
            case "oujda", "tétouan", "meknès":
                this.fraisLivraison = 45.0;
                break;
            default:
                this.fraisLivraison = 50.0;
        }

        return "Frais de livraison pour " + ville + ": " + this.fraisLivraison + " DH";
    }

    // Méthode getInfosSuivi
    public String getInfosSuivi() {
        StringBuilder info = new StringBuilder();
        info.append("=== Informations de Livraison ===\n");
        info.append("ID: ").append(idLivraison).append("\n");
        info.append("Adresse: ").append(adresse != null ? adresse : "Non définie").append("\n");
        info.append("Ville: ").append(ville != null ? ville : "Non définie").append("\n");
        if (codePostal != null) {
            info.append("Code Postal: ").append(codePostal).append("\n");
        }
        if (telephone != null) {
            info.append("Téléphone: ").append(telephone).append("\n");
        }
        info.append("Transporteur: ").append(transporteur != null ? transporteur : "Non assigné").append("\n");
        info.append("Frais: ").append(fraisLivraison).append(" DH\n");

        if (dateEnvoi != null) {
            info.append("Date d'envoi: ").append(dateEnvoi).append("\n");
        }
        if (dateEstimee != null) {
            info.append("Date estimée: ").append(dateEstimee).append("\n");
        }

        if (commande != null) {
            info.append("Commande: ").append(commande.getRefCommande()).append("\n");
            info.append("Statut: ").append(commande.getStatut()).append("\n");
        }

        return info.toString();
    }
}
