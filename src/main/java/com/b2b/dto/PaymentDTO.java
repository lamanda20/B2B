package com.b2b.dto;

import com.b2b.model.Payment;
import lombok.Data;

@Data
public class PaymentDTO {

    private Long id;
    private String orderId;
    private Double amount;
    private String method;
    private String status;
    private String date;
    private String reference;
    private String transactionId;

    public PaymentDTO(Payment payment) {
        this.id = payment.getId();
        this.orderId = payment.getOrderId();
        this.amount = payment.getAmount();
        this.method = payment.getMethod();
        this.status = payment.getStatus() != null ? payment.getStatus().getLabel() : null;
        this.date = payment.getDate() != null ? payment.getDate().toString() : null;
        this.reference = payment.getReference();
        this.transactionId = payment.getTransactionId();
    }
}