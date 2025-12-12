// src/main/java/com/b2b/service/impl/CommandeServiceImpl.java
package com.b2b.service.impl;

import com.b2b.model.*;
import com.b2b.repository.CommandeRepository;
import com.b2b.repository.CompanyRepository;
import com.b2b.repository.LigneCommandeRepository;
import com.b2b.repository.ProduitRepository;
import com.b2b.service.CommandeService;
import com.b2b.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class CommandeServiceImpl implements CommandeService {

    private final CommandeRepository commandeRepository;
    private final LigneCommandeRepository ligneCommandeRepository;
    private final NotificationService notificationService;
    private final CompanyRepository companyRepository;
    private final ProduitRepository produitRepository;

    @Override
    public List<Commande> findAll() {
        return commandeRepository.findAll();
    }

    @Override
    public Optional<Commande> findById(Long id) {
        return commandeRepository.findById(id);
    }

    @Override
    public Optional<Commande> findByRefCommande(String refCommande) {
        return commandeRepository.findByRefCommande(refCommande);
    }

    @Override
    public List<Commande> findByCompany(Long companyId) {
        return commandeRepository.findByCompanyId(companyId);
    }

    @Override
    public List<Commande> findForSeller(Long sellerCompanyId) {
        return commandeRepository.findSellerOrders(sellerCompanyId);
    }

    @Override
    public Commande create(Commande commande) {

        // 1) Ensure reference + date set
        if (commande.getRefCommande() == null || commande.getRefCommande().isBlank()) {
            String ref = "CMD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
            commande.setRefCommande(ref);
        }
        if (commande.getDateCommande() == null) {
            commande.setDateCommande(LocalDate.now());
        }

        // 2) Reload buyer from DB (JSON only gives buyer ID)
        Long buyerId = commande.getCompany().getId();
        Company buyer = companyRepository.findById(buyerId)
                .orElseThrow(() -> new RuntimeException("Buyer company not found"));
        commande.setCompany(buyer);

        // 3) Rebuild lignes with REAL product entities
        List<LigneCommande> rebuilt = commande.getLignes().stream()
                .map(l -> {
                    Produit p = produitRepository.findById(l.getProduit().getId())
                            .orElseThrow(() -> new RuntimeException("Product not found"));

                    LigneCommande nl = new LigneCommande();
                    nl.setProduit(p);
                    nl.setQuantite(l.getQuantite());
                    nl.setPrixUnitaire(l.getPrixUnitaire());
                    nl.setCommande(commande);
                    return nl;
                }).collect(Collectors.toList());

        commande.setLignes(rebuilt);

        // 4) Save everything
        Commande saved = commandeRepository.save(commande);

        // 5) Detect seller (first product's company)
        Long sellerId = null;
        if (!saved.getLignes().isEmpty()) {
            sellerId = saved.getLignes().get(0).getProduit().getCompany().getId();
        }

        // 6) Notify seller
        if (sellerId != null) {
            notificationService.sendToCompany(
                    sellerId,
                    "Nouvelle commande " + saved.getRefCommande()
                            + " de " + buyer.getName()
            );
        }

        return saved;
    }

    @Override
    public Commande ajouterLigneCommande(Long commandeId, LigneCommande ligne) {
        Commande c = commandeRepository.findById(commandeId)
                .orElseThrow(() -> new RuntimeException("Commande introuvable"));
        c.ajouterLigne(ligne);
        return commandeRepository.save(c);
    }

    @Override
    public double calculerTotal(Long commandeId) {
        Commande c = commandeRepository.findById(commandeId)
                .orElseThrow(() -> new RuntimeException("Commande introuvable"));
        return c.calculerTotal();
    }

    @Override
    public StatutCommande validerCommande(Long commandeId) {
        Commande c = commandeRepository.findById(commandeId)
                .orElseThrow(() -> new RuntimeException("Commande introuvable"));
        c.validerCommande();
        commandeRepository.save(c);

        // Notify buyer
        if (c.getCompany() != null) {
            notificationService.sendToCompany(
                    c.getCompany().getId(),
                    "Votre commande " + c.getRefCommande() + " a été validée."
            );
        }
        return c.getStatut();
    }

    @Override
    public void afficherCommande(Long commandeId) {
        commandeRepository.findById(commandeId)
                .ifPresent(Commande::afficherCommande);
    }

    @Override
    public Commande updateStatut(Long commandeId, StatutCommande statut) {
        Commande c = commandeRepository.findById(commandeId)
                .orElseThrow(() -> new RuntimeException("Commande introuvable"));
        c.setStatut(statut);
        Commande saved = commandeRepository.save(c);

        // Simple notification to buyer depending on statut
        if (c.getCompany() != null) {
            String msg;
            switch (statut) {
                case VALIDEE ->
                        msg = "Votre commande " + c.getRefCommande() + " a été acceptée.";
                case ANNULEE ->
                        msg = "Votre commande " + c.getRefCommande() + " a été refusée.";
                default ->
                        msg = "Le statut de la commande " + c.getRefCommande() + " est passé à " + statut;
            }
            notificationService.sendToCompany(c.getCompany().getId(), msg);
        }
        return saved;
    }

    @Override
    public void delete(Long id) {
        commandeRepository.deleteById(id);
    }

    @Override
    public List<Commande> findOrdersForSeller(Long sellerId) {
        return commandeRepository.findAll()
                .stream()
                .filter(c ->
                        c.getLignes().stream()
                                .anyMatch(l ->
                                        l.getProduit().getCompany().getId().equals(sellerId)
                                )
                )
                .collect(Collectors.toList());
    }


    @Override
    public Commande updateStatus(Long orderId, StatutCommande statut) {
        return commandeRepository.findById(orderId).map(cmd -> {
            cmd.setStatut(statut);

            // Reduce stock ONLY if accepted
            if (statut == StatutCommande.VALIDEE) {
                for (LigneCommande lc : cmd.getLignes()) {
                    Produit p = lc.getProduit();
                    if (p != null) {
                        p.setStock(p.getStock() - lc.getQuantite());
                    }
                }
            }

            if (statut == StatutCommande.VALIDEE) {
                notificationService.sendToCompany(
                        cmd.getCompany().getId(),
                        "Votre commande #" + cmd.getRefCommande() + " a été ACCEPTÉE."
                );
            }

            if (statut == StatutCommande.REFUSEE) {
                notificationService.sendToCompany(
                        cmd.getCompany().getId(),
                        "Votre commande #" + cmd.getRefCommande() + " a été REFUSÉE."
                );
            }


            return commandeRepository.save(cmd);
        }).orElseThrow(() -> new RuntimeException("Commande introuvable"));
    }



}
