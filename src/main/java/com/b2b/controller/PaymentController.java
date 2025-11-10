package com.b2b.controller;

import com.b2b.dto.PaymentDTO;
import com.b2b.model.PaymentStatus;
import com.b2b.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Contrôleur REST pour gérer les paiements
 * Endpoints accessibles via /api/payments/**
 */
@RestController
@RequestMapping("/payments")
@CrossOrigin(originPatterns = "*", allowCredentials = "true")
public class PaymentController {

    private final PaymentService paymentService;

    @Autowired
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    /**
     * GET /api/payments - Liste tous les paiements
     */
    @GetMapping
    public ResponseEntity<List<PaymentDTO>> getAllPayments() {
        List<PaymentDTO> payments = paymentService.getAllPayments();
        return ResponseEntity.ok(payments);
    }

    /**
     * GET /api/payments/{id} - Détails d’un paiement
     */
    @GetMapping("/{id}")
    public ResponseEntity<PaymentDTO> getPaymentById(@PathVariable Long id) {
        return paymentService.getPaymentById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * POST /api/payments - Créer un paiement
     */
    @PostMapping
    public ResponseEntity<PaymentDTO> createPayment(@RequestBody Map<String, Object> paymentData) {
        try {
            PaymentDTO payment = paymentService.createPayment(paymentData);
            return ResponseEntity.status(HttpStatus.CREATED).body(payment);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * PUT /api/payments/{id}/validate - Valider un paiement
     */
    @PutMapping("/{id}/validate")
    public ResponseEntity<PaymentDTO> validatePayment(@PathVariable Long id) {
        PaymentDTO payment = paymentService.validatePayment(id);
        return ResponseEntity.ok(payment);
    }

    /**
     * PUT /api/payments/{id}/cancel - Annuler un paiement
     */
    @PutMapping("/{id}/cancel")
    public ResponseEntity<PaymentDTO> cancelPayment(@PathVariable Long id) {
        PaymentDTO payment = paymentService.cancelPayment(id);
        return ResponseEntity.ok(payment);
    }

    /**
     * GET /api/payments/status/{status} - Filtrer par statut
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<List<PaymentDTO>> getPaymentsByStatus(@PathVariable String status) {
        try {
            PaymentStatus statut = PaymentStatus.valueOf(status.toUpperCase());
            List<PaymentDTO> payments = paymentService.getPaymentsByStatus(statut);
            return ResponseEntity.ok(payments);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
