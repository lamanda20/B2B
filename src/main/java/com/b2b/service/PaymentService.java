package com.b2b.service;

import com.b2b.dto.PaymentDTO;
import com.b2b.model.Payment;

import java.util.List;
import java.util.Optional;

public interface PaymentService {
    List<PaymentDTO> getAllPayments();
    PaymentDTO createPayment(Payment payment);
    PaymentDTO validatePayment(Long id);
    PaymentDTO cancelPayment(Long id);
    Optional<PaymentDTO> findByTransactionId(String transactionId);
}
