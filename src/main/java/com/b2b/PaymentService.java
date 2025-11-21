package com.b2b.service;

import com.b2b.dto.PaymentDTO;
import com.b2b.model.Payment;
import com.b2b.model.PaymentStatus;
import com.b2b.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    public List<PaymentDTO> getAllPayments() {
        return paymentRepository.findAll().stream()
                .map(PaymentDTO::new)
                .collect(Collectors.toList());
    }

    public PaymentDTO createPayment(Payment payment) {
        payment.setStatus(PaymentStatus.EN_ATTENTE);
        payment.setDate(LocalDateTime.now());
        Payment saved = paymentRepository.save(payment);
        return new PaymentDTO(saved);
    }

    public PaymentDTO validatePayment(Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Paiement non trouvé"));
        if (payment.getStatus() != PaymentStatus.EN_ATTENTE) {
            throw new RuntimeException("Paiement ne peut pas être validé (statut actuel : " + payment.getStatus().getLabel() + ")");
        }
        payment.setStatus(PaymentStatus.VALIDÉ);
        payment.setValidationDate(LocalDateTime.now());
        payment.setHistory((payment.getHistory() != null ? payment.getHistory() + "\n" : "") + "Validé le " + LocalDateTime.now());
        Payment saved = paymentRepository.save(payment);
        return new PaymentDTO(saved);
    }

    public PaymentDTO cancelPayment(Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Paiement non trouvé"));
        if (payment.getStatus() != PaymentStatus.EN_ATTENTE) {
            throw new RuntimeException("Paiement ne peut pas être annulé (statut actuel : " + payment.getStatus().getLabel() + ")");
        }
        payment.setStatus(PaymentStatus.REFUSÉ);
        payment.setHistory((payment.getHistory() != null ? payment.getHistory() + "\n" : "") + "Annulé le " + LocalDateTime.now());
        Payment saved = paymentRepository.save(payment);
        return new PaymentDTO(saved);
    }

    public Optional<PaymentDTO> findByTransactionId(String transactionId) {
        return paymentRepository.findByTransactionId(transactionId)
                .map(PaymentDTO::new);
    }
}