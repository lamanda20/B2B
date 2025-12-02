package com.b2b.service;

import com.b2b.dto.CartDto;

public interface CartService {

    CartDto getCart(Long companyId);

    CartDto addToCart(Long companyId, Long productId, int qty);

    CartDto updateQuantity(Long companyId, Long productId, int qty);

    CartDto removeItem(Long companyId, Long productId);

    void clearCart(Long companyId);
}
