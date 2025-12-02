package com.b2b.controller;

import com.b2b.dto.CartDto;
import com.b2b.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping("/add")
    public CartDto addToCart(
            @RequestParam Long companyId,
            @RequestParam Long productId,
            @RequestParam Integer qty
    ) {
        return cartService.addToCart(companyId, productId, qty);
    }

    @GetMapping("/{companyId}")
    public CartDto getCart(@PathVariable Long companyId) {
        return cartService.getCart(companyId);
    }

    @PutMapping("/update")
    public CartDto updateQuantity(
            @RequestParam Long companyId,
            @RequestParam Long productId,
            @RequestParam Integer qty
    ) {
        return cartService.updateQuantity(companyId, productId, qty);
    }

    @DeleteMapping("/remove")
    public CartDto removeFromCart(
            @RequestParam Long companyId,
            @RequestParam Long productId
    ) {
        return cartService.removeItem(companyId, productId);
    }

    @DeleteMapping("/clear/{companyId}")
    public void clearCart(@PathVariable Long companyId) {
        cartService.clearCart(companyId);
    }
}
