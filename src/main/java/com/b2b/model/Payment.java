package com.b2b.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String orderId;  // Référence à la commande (ex. : ID de Commande)
    private Double amount;
    private String method;   // Ex. : "CARTE_BANCAIRE", "VIREMENT", etc.
    private String reference;
    private String notes;
    private String transactionId;
    private LocalDateTime date;  // Date de création/validation
    private LocalDateTime validationDate;
    private String history;  // Historique des actions (ex. : "Validé le 2023-10-01")

    @Enumerated(EnumType.STRING)
    private PaymentStatus status = PaymentStatus.EN_ATTENTE;  // Statut par défaut

    // Relations
    @ManyToOne
    @JoinColumn(name = "Company_id")
    private Company company;

    @ManyToOne
    @JoinColumn(name = "commande_id")
    private Commande commande;

    // Relation optionnelle vers Delivery (pour lier paiement et livraison)
    @OneToOne
    @JoinColumn(name = "delivery_id")
    private Livraison delivery;

    // Constructeur pour création (comme dans votre frontend)
    public Payment(String orderId, Double amount, String method, String reference, String notes) {
        this.orderId = orderId;
        this.amount = amount;
        this.method = method;
        this.reference = reference;
        this.notes = notes;
        this.date = LocalDateTime.now();
    }
}