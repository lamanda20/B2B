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

    private String orderId;
    private Double amount;
    private String method;
    private String reference;
    private String notes;
    private String transactionId;
    private LocalDateTime date;
    private LocalDateTime validationDate;
    private String history;

    @Enumerated(EnumType.STRING)
    private StatutPaiement status = StatutPaiement.EN_ATTENTE;

    @ManyToOne
    @JoinColumn(name = "commande_id")
    private Commande commande;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "delivery_id")
    private Long deliveryId;

    public Payment(String orderId, Double amount, String method, String reference, String notes) {
        this.orderId = orderId;
        this.amount = amount;
        this.method = method;
        this.reference = reference;
        this.notes = notes;
        this.date = LocalDateTime.now();
    }
}