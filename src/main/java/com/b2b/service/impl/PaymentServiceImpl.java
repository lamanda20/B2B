package com.b2b.service.impl;

import com.b2b.model.Payment;
import com.b2b.model.StatutPaiement;
import com.b2b.repository.PaymentRepository;
import com.b2b.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;

    @Autowired
    public PaymentServiceImpl(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Override
    public List<Payment> findAll() {
        return paymentRepository.findAll();
    }

    @Override
    public Optional<Payment> findById(Long id) {
        return paymentRepository.findById(id);
    }

    @Override
    public List<Payment> findByCompany(Long companyId) {
        return paymentRepository.findByCompanyId(companyId);
    }

    @Override
    public List<Payment> findByCommande(Long commandeId) {
        return paymentRepository.findByCommandeId(commandeId);
    }

    @Override
    public List<Payment> findByStatus(StatutPaiement status) {
        return paymentRepository.findByStatus(status);
    }

    @Override
    public Payment create(Payment payment) {
        return paymentRepository.save(payment);
    }

    @Override
    public boolean effectuerPaiement(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Paiement non trouvé"));
        boolean success = payment.effectuerPaiement();
        paymentRepository.save(payment);
        return success;
    }

    @Override
    public String getStatutPaiement(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Paiement non trouvé"));
        return payment.getStatutPaiement();
    }

    @Override
    public double calculerMontant(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Paiement non trouvé"));
        return payment.calculerMontant();
    }

    @Override
    public void delete(Long id) {
        paymentRepository.deleteById(id);
    }
}
