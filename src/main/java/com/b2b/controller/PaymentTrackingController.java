package com.b2b.controller;

import com.b2b.dto.PaymentTrackingDTO;
import com.b2b.exception.ResourceNotFoundException;
import com.b2b.model.Payment;
import com.b2b.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Contrôleur REST pour le suivi des paiements côté client
 */
@RestController
@RequestMapping("/api/tracking")
@CrossOrigin(originPatterns = "*", allowCredentials = "true")
public class PaymentTrackingController {

    private final PaymentRepository paymentRepository;

    @Autowired
    public PaymentTrackingController(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    /**
     * GET /api/tracking/{reference} - Suivre un paiement via sa référence
     */
    @GetMapping("/{reference}")
    public ResponseEntity<PaymentTrackingDTO> getPaymentTracking(@PathVariable String reference) {
        Payment payment = paymentRepository.findByReference(reference)
                .orElseThrow(() -> new ResourceNotFoundException("Paiement non trouvé : " + reference));

        PaymentTrackingDTO dto = new PaymentTrackingDTO(
                payment.getReference(),
                payment.getStatus(),
                payment.getAmount(),
                payment.getDateCreated(),
                payment.getMethod()
        );

        return ResponseEntity.ok(dto);
    }
}
