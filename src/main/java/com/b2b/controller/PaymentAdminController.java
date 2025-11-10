package com.b2b.controller;

import com.b2b.exception.ResourceNotFoundException;
import com.b2b.model.PaymentStatus;
import com.b2b.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Contrôleur REST réservé à l'administrateur pour gérer les paiements
 */
@RestController
@RequestMapping("/api/admin/payments")
@CrossOrigin(originPatterns = "*", allowCredentials = "true")
public class PaymentAdminController {

    private final PaymentService paymentService;

    @Autowired
    public PaymentAdminController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    /**
     * PUT /api/admin/payments/status/{paymentId} - Met à jour le statut d’un paiement
     */
    @PutMapping("/status/{paymentId}")
    public ResponseEntity<Void> updatePaymentStatus(
            @PathVariable Long paymentId,
            @RequestBody String nouveauStatut) {

        try {
            PaymentStatus statutEnum = PaymentStatus.valueOf(nouveauStatut.toUpperCase());
            paymentService.updatePaymentStatus(paymentId, statutEnum);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            throw new ResourceNotFoundException("Statut invalide : " + nouveauStatut);
        }
    }
}
