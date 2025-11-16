package com.b2b.controller;

import com.b2b.model.Payment;
import com.b2b.model.StatutPaiement;
import com.b2b.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/api/payments")
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
    public ResponseEntity<List<Payment>> findAll() {
        return ResponseEntity.ok(paymentService.findAll());
    }

    /**
     * GET /api/payments/{id} - Détails d'un paiement
     */
    @GetMapping("/{id}")
    public ResponseEntity<Payment> findById(@PathVariable Long id) {
        return paymentService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * GET /api/payments/Company/{CompanyId} - Paiements d'un Company
     */
    @GetMapping("/Company/{CompanyId}")
    public ResponseEntity<List<Payment>> findByCompany(@PathVariable Long CompanyId) {
        return ResponseEntity.ok(paymentService.findByCompany(CompanyId));
    }

    /**
     * GET /api/payments/commande/{commandeId} - Paiements d'une commande
     */
    @GetMapping("/commande/{commandeId}")
    public ResponseEntity<List<Payment>> findByCommande(@PathVariable Long commandeId) {
        return ResponseEntity.ok(paymentService.findByCommande(commandeId));
    }

    /**
     * GET /api/payments/status/{status} - Paiements par statut
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Payment>> findByStatus(@PathVariable String status) {
        try {
            StatutPaiement statutEnum = StatutPaiement.valueOf(status.toUpperCase());
            return ResponseEntity.ok(paymentService.findByStatus(statutEnum));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * POST /api/payments - Créer un nouveau paiement
     */
    @PostMapping
    public ResponseEntity<Payment> create(@RequestBody Payment payment) {
        Payment created = paymentService.create(payment);
        return ResponseEntity.ok(created);
    }

    /**
     * POST /api/payments/{id}/effectuer - Effectuer un paiement
     */
    @PostMapping("/{id}/effectuer")
    public ResponseEntity<Map<String, Object>> effectuerPaiement(@PathVariable Long id) {
        try {
            boolean success = paymentService.effectuerPaiement(id);
            Map<String, Object> response = new HashMap<>();
            response.put("success", success);
            response.put("message", success ? "Paiement effectué avec succès" : "Échec du paiement");
            response.put("statut", paymentService.getStatutPaiement(id));
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * GET /api/payments/{id}/statut - Obtenir le statut d'un paiement
     */
    @GetMapping("/{id}/statut")
    public ResponseEntity<Map<String, String>> getStatutPaiement(@PathVariable Long id) {
        try {
            String statut = paymentService.getStatutPaiement(id);
            Map<String, String> response = new HashMap<>();
            response.put("statut", statut);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * GET /api/payments/{id}/montant - Calculer le montant d'un paiement
     */
    @GetMapping("/{id}/montant")
    public ResponseEntity<Map<String, Double>> calculerMontant(@PathVariable Long id) {
        try {
            double montant = paymentService.calculerMontant(id);
            Map<String, Double> response = new HashMap<>();
            response.put("montant", montant);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * DELETE /api/payments/{id} - Supprimer un paiement
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        paymentService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

