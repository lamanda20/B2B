package com.b2b.service;

import com.b2b.model.Payment;
import com.b2b.model.StatutPaiement;

import java.util.List;
import java.util.Optional;

public interface PaymentService {
    List<Payment> findAll();
    Optional<Payment> findById(Long id);
    List<Payment> findByCompany(Long companyId);
    List<Payment> findByCommande(Long commandeId);
    List<Payment> findByStatus(StatutPaiement status);
    Payment create(Payment payment);
    boolean effectuerPaiement(Long paymentId);
    String getStatutPaiement(Long paymentId);
    double calculerMontant(Long paymentId);
    void delete(Long id);
}
