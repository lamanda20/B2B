package com.b2b.service.impl;

import com.b2b.model.*;
import com.b2b.repository.CartItemRepository;
import com.b2b.repository.CartRepository;
import com.b2b.repository.CommandeRepository;
import com.b2b.repository.LigneCommandeRepository;
import com.b2b.repository.ProduitRepository;
import com.b2b.repository.CompanyRepository;
import com.b2b.service.CheckoutService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@Transactional
@RequiredArgsConstructor
public class CheckoutServiceImpl implements CheckoutService {

    private final CommandeRepository commandeRepository;
    private final LigneCommandeRepository ligneCommandeRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProduitRepository produitRepository;
    private final CompanyRepository companyRepository;

    @Override
    public Commande placeOrder(Long companyId) {

        // 1) Load company
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new RuntimeException("Entreprise introuvable"));

        // 2) Load cart
        var cart = cartRepository.findByCompanyId(companyId)
                .orElseThrow(() -> new RuntimeException("Aucun panier pour cette entreprise"));

        if (cart.getItems().isEmpty()) {
            throw new RuntimeException("Le panier est vide");
        }

        // 3) Create Commande
        Commande commande = new Commande();
        commande.setCompany(company);
        commande.setDateCommande(LocalDate.now());
        commande.setRefCommande("CMD-" + System.currentTimeMillis());
        commande.setStatut(StatutCommande.EN_COURS);

        commande = commandeRepository.save(commande);

        // 4) Convert items → LigneCommandes
        for (var item : cart.getItems()) {

            Produit produit = produitRepository.findById(item.getProductId())
                    .orElseThrow(() -> new RuntimeException("Produit introuvable"));

            LigneCommande ligne = new LigneCommande();
            ligne.setCommande(commande);
            ligne.setProduit(produit);
            ligne.setQuantite(item.getQuantity());
            ligne.setPrixUnitaire(item.getPrice());

            ligneCommandeRepository.save(ligne);
        }

        // 5) Clear cart
        cartItemRepository.deleteAll(cart.getItems());
        cart.getItems().clear();
        cartRepository.save(cart);

        return commande;
    }
}
