package com.b2b.service.impl;

import com.b2b.dto.CategoryStatsDTO;
import com.b2b.dto.CompanyStatsDTO;
import com.b2b.dto.ProductStatsDTO;
import com.b2b.model.*;
import com.b2b.repository.CommandeRepository;
import com.b2b.repository.CompanyRepository;
import com.b2b.repository.ProduitRepository;
import com.b2b.service.StatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {

    private final CommandeRepository commandeRepository;
    private final ProduitRepository produitRepository;

    // ============================================================
    //              HELPER : toujours retourner TOP3
    // ============================================================

    private <T> List<T> ensureTop3(List<T> list, T placeholder) {

        if (list == null) list = new ArrayList<>();

        // Si vide → remplir 3 fois
        if (list.isEmpty()) {
            return List.of(placeholder, placeholder, placeholder);
        }

        // Si < 3 → compléter
        List<T> result = new ArrayList<>(list);
        while (result.size() < 3) {
            result.add(placeholder);
        }

        // Si > 3 → tronquer
        return result.subList(0, 3);
    }


    // ============================================================
    //                BEST COMPANIES ACHETEUSES
    // ============================================================

    @Override
    public List<CompanyStatsDTO> bestCompaniesAcheteuses() {

        Map<Company, Double> totals = new HashMap<>();
        List<Commande> commandes = commandeRepository.findAll();

        for (Commande cmd : commandes) {
            Company acheteur = cmd.getCompany();
            double total = cmd.calculerTotal();
            totals.put(acheteur, totals.getOrDefault(acheteur, 0.0) + total);
        }

        return totals.entrySet().stream()
                .sorted(Map.Entry.<Company, Double>comparingByValue().reversed())
                .map(e -> new CompanyStatsDTO(
                        e.getKey().getId(),
                        e.getKey().getName(),
                        e.getValue()
                ))
                .collect(Collectors.toList());
    }


    @Override
    public List<CompanyStatsDTO> top3CompaniesAcheteuses() {

        CompanyStatsDTO placeholder = new CompanyStatsDTO(
                0L, "Aucune entreprise", 0.0
        );

        return ensureTop3(bestCompaniesAcheteuses(), placeholder);
    }


    // ============================================================
    //                BEST COMPANIES VENDEUSES
    // ============================================================

    @Override
    public List<CompanyStatsDTO> bestCompaniesVendeuses() {

        Map<Company, Double> totals = new HashMap<>();
        List<Commande> commandes = commandeRepository.findAll();

        for (Commande cmd : commandes) {
            for (LigneCommande ligne : cmd.getLignes()) {
                if(ligne.getProduit() != null) {
                    Company vendeur = ligne.getProduit().getCompany();
                    double total = ligne.getSousTotal();
                    totals.put(vendeur, totals.getOrDefault(vendeur, 0.0) + total);
                }
            }
        }

        return totals.entrySet().stream()
                .sorted(Map.Entry.<Company, Double>comparingByValue().reversed())
                .map(e -> new CompanyStatsDTO(
                        e.getKey().getId(),
                        e.getKey().getName(),
                        e.getValue()
                ))
                .collect(Collectors.toList());
    }


    @Override
    public List<CompanyStatsDTO> top3CompaniesVendeuses() {

        CompanyStatsDTO placeholder = new CompanyStatsDTO(
                0L, "Aucune entreprise", 0.0
        );

        return ensureTop3(bestCompaniesVendeuses(), placeholder);
    }


    // ============================================================
    //                    BEST PRODUCTS
    // ============================================================

    @Override
    public List<ProductStatsDTO> bestProducts() {

        Map<Produit, Double> totals = new HashMap<>();
        List<Commande> commandes = commandeRepository.findAll();

        for (Commande cmd : commandes) {
            for (LigneCommande ligne : cmd.getLignes()) {
                if(ligne.getProduit() != null) {
                    Produit p = ligne.getProduit();
                    double total = ligne.getSousTotal();
                    totals.put(p, totals.getOrDefault(p, 0.0) + total);
                }
            }
        }

        return totals.entrySet().stream()
                .sorted(Map.Entry.<Produit, Double>comparingByValue().reversed())
                .map(e -> new ProductStatsDTO(
                        e.getKey().getId(),
                        e.getKey().getName(),
                        e.getValue()
                ))
                .collect(Collectors.toList());
    }


    @Override
    public List<ProductStatsDTO> top3Products() {

        ProductStatsDTO placeholder = new ProductStatsDTO(
                0L, "Aucun produit", 0.0
        );

        return ensureTop3(bestProducts(), placeholder);
    }


    // ============================================================
    //                    BEST CATEGORIES
    // ============================================================

    @Override
    public List<CategoryStatsDTO> bestCategories() {

        Map<Categorie, Double> totals = new HashMap<>();
        List<Commande> commandes = commandeRepository.findAll();

        for (Commande cmd : commandes) {
            for (LigneCommande ligne : cmd.getLignes()) {
                if(ligne.getProduit() != null) {
                    Categorie cat = ligne.getProduit().getCategorie();
                    double total = ligne.getSousTotal();
                    totals.put(cat, totals.getOrDefault(cat, 0.0) + total);
                }
            }
        }

        return totals.entrySet().stream()
                .sorted(Map.Entry.<Categorie, Double>comparingByValue().reversed())
                .map(e -> new CategoryStatsDTO(
                        e.getKey().getIdCat(),
                        e.getKey().getName(),
                        e.getValue()
                ))
                .collect(Collectors.toList());
    }


    @Override
    public List<CategoryStatsDTO> top3Categories() {

        CategoryStatsDTO placeholder = new CategoryStatsDTO(
                0, "Aucune catégorie", 0.0
        );

        return ensureTop3(bestCategories(), placeholder);
    }


    // ============================================================
    //                      NOMBRE
    // ============================================================

    @Override
    public int nombreCompaniesActives() {

        Set<Company> actives = new HashSet<>();
        List<Commande> commandes = commandeRepository.findAll();

        if(!commandes.isEmpty()) {
            for (Commande cmd : commandes) {

                actives.add(cmd.getCompany());

                for (LigneCommande ligne : cmd.getLignes()) {
                    if(ligne.getProduit() != null) {
                        actives.add(ligne.getProduit().getCompany());
                    }
                }
            }
        }

        return actives.size();
    }

    @Override
    public int nombreProduits() {
        return (int) produitRepository.count();
    }

    @Override
    public int nombreCommadesPasser() {
        return (int) commandeRepository.count();
    }
}
