package com.b2b.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
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
    private StatutPaiement status = StatutPaiement.EN_ATTENTE;  // Statut par défaut

    // Remplace la clé étrangère par une relation ManyToOne afin que Commande.paiements (mappedBy = "commande") fonctionne
    @ManyToOne
    @JoinColumn(name = "commande_id")
    private Commande commande;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "delivery_id")
    private Long deliveryId;

    // Constructeurs
    public Payment() {}

    public Payment(Long id, String orderId, Double amount, String method, String reference, String notes, String transactionId, LocalDateTime date, LocalDateTime validationDate, String history, StatutPaiement status, Long userId, Long deliveryId, Commande commande) {
        this.id = id;
        this.orderId = orderId;
        this.amount = amount;
        this.method = method;
        this.reference = reference;
        this.notes = notes;
        this.transactionId = transactionId;
        this.date = date;
        this.validationDate = validationDate;
        this.history = history;
        this.status = status;
        this.userId = userId;
        this.deliveryId = deliveryId;
        this.commande = commande;
    }

    // Constructeur pour création (comme dans votre frontend)
    public Payment(String orderId, Double amount, String method, String reference, String notes) {
        this.orderId = orderId;
        this.amount = amount;
        this.method = method;
        this.reference = reference;
        this.notes = notes;
        this.date = LocalDateTime.now();
    }

    // Getters et setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }

    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }

    public String getMethod() { return method; }
    public void setMethod(String method) { this.method = method; }

    public String getReference() { return reference; }
    public void setReference(String reference) { this.reference = reference; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }

    public LocalDateTime getDate() { return date; }
    public void setDate(LocalDateTime date) { this.date = date; }

    public LocalDateTime getValidationDate() { return validationDate; }
    public void setValidationDate(LocalDateTime validationDate) { this.validationDate = validationDate; }

    public String getHistory() { return history; }
    public void setHistory(String history) { this.history = history; }

    public StatutPaiement getStatus() { return status; }
    public void setStatus(StatutPaiement status) { this.status = status; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Long getDeliveryId() { return deliveryId; }
    public void setDeliveryId(Long deliveryId) { this.deliveryId = deliveryId; }

    public Commande getCommande() { return commande; }
    public void setCommande(Commande commande) { this.commande = commande; }
}