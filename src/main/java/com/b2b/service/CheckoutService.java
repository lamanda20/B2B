package com.b2b.service;

import com.b2b.model.Commande;

public interface CheckoutService {
    Commande placeOrder(Long companyId);
}
