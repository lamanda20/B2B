package com.b2b.repository;

import com.b2b.model.Payment;
import com.b2b.model.StatutPaiement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findByCompanyId(Long CompanyId);
    List<Payment> findByCommandeId(Long commandeId);
    List<Payment> findByStatus(StatutPaiement status);
}

