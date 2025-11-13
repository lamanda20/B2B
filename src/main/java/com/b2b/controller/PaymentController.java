package com.b2b.controller;

import com.b2b.dto.PaymentDTO;
import com.b2b.model.Payment;
import com.b2b.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/payments")
@CrossOrigin(originPatterns = "*", allowCredentials = "true")  // CORRIGÉ: originPatterns au lieu de origins
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @GetMapping
    public ResponseEntity<List<PaymentDTO>> getAllPayments() {
        List<PaymentDTO> payments = paymentService.getAllPayments();
        return ResponseEntity.ok(payments);
    }

    @PostMapping
    public ResponseEntity<PaymentDTO> createPayment(@RequestBody Payment payment) {
        try {
            PaymentDTO created = paymentService.createPayment(payment);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}/validate")
    public ResponseEntity<PaymentDTO> validatePayment(@PathVariable Long id) {
        try {
            PaymentDTO validated = paymentService.validatePayment(id);
            return ResponseEntity.ok(validated);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<PaymentDTO> cancelPayment(@PathVariable Long id) {
        try {
            PaymentDTO canceled = paymentService.cancelPayment(id);
            return ResponseEntity.ok(canceled);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/search")
    public ResponseEntity<PaymentDTO> searchByTransactionId(@RequestParam String transactionId) {
        Optional<PaymentDTO> payment = paymentService.findByTransactionId(transactionId);
        return payment.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}