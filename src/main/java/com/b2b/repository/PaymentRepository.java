package com.b2b.repository;

import com.b2b.model.Payment;
import com.b2b.model.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    // Recherche par transactionId (pour le frontend)
    Optional<Payment> findByTransactionId(String transactionId);

    // Recherche par statut
    List<Payment> findByStatus(PaymentStatus status);

    // Recherche par commande
    List<Payment> findByCommandeId(Long commandeId);

    // Recherche par user
    List<Payment> findByUserId(Long userId);

    // Recherche par orderId (si orderId est unique)
    Optional<Payment> findByOrderId(String orderId);
}