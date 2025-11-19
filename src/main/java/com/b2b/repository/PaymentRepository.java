package com.b2b.repository;

import com.b2b.model.Payment;
import com.b2b.model.StatutPaiement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Optional<Payment> findByTransactionId(String transactionId);
    List<Payment> findByStatus(StatutPaiement status);
    List<Payment> findByCommandeId(Long commandeId);
    List<Payment> findByUserId(Long userId);
    Optional<Payment> findByOrderId(String orderId);
}