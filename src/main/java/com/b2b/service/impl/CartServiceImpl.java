package com.b2b.service.impl;

import com.b2b.dto.CartDto;
import com.b2b.dto.CartItemDto;
import com.b2b.model.Cart;
import com.b2b.model.CartItem;
import com.b2b.model.Produit;
import com.b2b.repository.CartRepository;
import com.b2b.repository.ProduitRepository;
import com.b2b.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final ProduitRepository produitRepository;

    // ==============================================================
    // FIND OR CREATE CART
    // ==============================================================
    private Cart getOrCreateCart(Long companyId) {
        return cartRepository.findByCompanyId(companyId)
                .orElseGet(() -> {
                    Cart c = new Cart();
                    c.setCompanyId(companyId);
                    return cartRepository.save(c);
                });
    }

    // ==============================================================
    // GET CART
    // ==============================================================
    @Override
    public CartDto getCart(Long companyId) {
        Cart cart = getOrCreateCart(companyId);
        return convertToDto(cart);
    }

    // ==============================================================
    // ADD ITEM TO CART
    // ==============================================================
    @Override
    public CartDto addToCart(Long companyId, Long productId, int qty) {

        Cart cart = getOrCreateCart(companyId);
        Produit produit = produitRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // Check if item exists
        CartItem item = cart.getItems().stream()
                .filter(i -> i.getProductId().equals(productId))
                .findFirst()
                .orElse(null);

        if (item == null) {
            item = new CartItem();
            item.setProductId(productId);
            item.setQuantity(qty);
            item.setPrice(produit.getPrice()); // snapshot
            item.setCart(cart);
            cart.getItems().add(item);
        } else {
            item.setQuantity(item.getQuantity() + qty);
        }

        cartRepository.save(cart);
        return convertToDto(cart);
    }

    // ==============================================================
    // UPDATE QTY
    // ==============================================================
    @Override
    public CartDto updateQuantity(Long companyId, Long productId, int qty) {

        Cart cart = getOrCreateCart(companyId);

        cart.getItems().removeIf(i -> {
            if (i.getProductId().equals(productId)) {
                if (qty <= 0) return true;       // remove item
                i.setQuantity(qty);
            }
            return false;
        });

        cartRepository.save(cart);
        return convertToDto(cart);
    }

    // ==============================================================
    // REMOVE ITEM
    // ==============================================================
    @Override
    public CartDto removeItem(Long companyId, Long productId) {

        Cart cart = getOrCreateCart(companyId);

        cart.getItems().removeIf(i -> i.getProductId().equals(productId));

        cartRepository.save(cart);
        return convertToDto(cart);
    }

    // ==============================================================
    // CLEAR CART
    // ==============================================================
    @Override
    public void clearCart(Long companyId) {
        Cart cart = getOrCreateCart(companyId);
        cart.getItems().clear();
        cartRepository.save(cart);
    }

    // ==============================================================
    // DTO CONVERSION
    // ==============================================================
    private CartDto convertToDto(Cart cart) {

        CartDto dto = new CartDto();
        dto.setCompanyId(cart.getCompanyId());

        dto.setItems(
                cart.getItems().stream().map(item -> {
                    Produit prod = produitRepository.findById(item.getProductId()).orElse(null);

                    CartItemDto ci = new CartItemDto();
                    ci.setProductId(item.getProductId());
                    ci.setQuantity(item.getQuantity());
                    ci.setUnitPrice(item.getPrice());
                    if (prod != null) {
                        ci.setName(prod.getName());
                        ci.setImageUrl(prod.getImageUrl());
                    }
                    return ci;
                }).collect(Collectors.toList())
        );

        double total = cart.getItems().stream()
                .mapToDouble(i -> i.getPrice() * i.getQuantity())
                .sum();

        dto.setSubtotal(total);

        return dto;
    }
}
