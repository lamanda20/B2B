package com.b2b.service.impl;

import com.b2b.dto.CategoryStatsDTO;
import com.b2b.dto.CompanyStatsDTO;
import com.b2b.dto.ProductStatsDTO;
import com.b2b.model.*;
        import com.b2b.repository.CommandeRepository;
import com.b2b.service.StatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
        import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {

    private final CommandeRepository commandeRepository;

    // Savoir les compagies fidelles ( Qui utilisisent notre apllication plusieur fois lors de l'achat )

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

    // TOP 3

    @Override
    public List<CompanyStatsDTO> top3CompaniesAcheteuses() {
        return bestCompaniesAcheteuses()
                .stream()
                .limit(3)
                .collect(Collectors.toList());
    }


    // Savoir les Best Sellers ( Qui gagne beaucoup d'argent d'apres notre plateforme )
    @Override
    public List<CompanyStatsDTO> bestCompaniesVendeuses() {

        Map<Company, Double> totals = new HashMap<>();
        List<Commande> commandes = commandeRepository.findAll();

        for (Commande cmd : commandes) {
            for (LigneCommande ligne : cmd.getLignes()) {
                Company vendeur = ligne.getProduit().getCompany();
                double total = ligne.getSousTotal();
                totals.put(vendeur, totals.getOrDefault(vendeur, 0.0) + total);
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

    // TOP 3

    @Override
    public List<CompanyStatsDTO> top3CompaniesVendeuses() {
        return bestCompaniesVendeuses()
                .stream()
                .limit(3)
                .collect(Collectors.toList());
    }


    // Les produit les plus vendus (Qui ont garanti une grand somme d'argent a leur company )

    @Override
    public List<ProductStatsDTO> bestProducts() {

        Map<Produit, Double> totals = new HashMap<>();
        List<Commande> commandes = commandeRepository.findAll();

        for (Commande cmd : commandes) {
            for (LigneCommande ligne : cmd.getLignes()) {
                Produit p = ligne.getProduit();
                double total = ligne.getSousTotal();
                totals.put(p, totals.getOrDefault(p, 0.0) + total);
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

    // TOP 3
    @Override
    public List<ProductStatsDTO> top3Products() {
        return bestProducts()
                .stream()
                .limit(3)
                .collect(Collectors.toList());
    }


    // les categorie qui sont un centre d'interet pour nos Companies
    @Override
    public List<CategoryStatsDTO> bestCategories() {

        Map<Categorie, Double> totals = new HashMap<>();
        List<Commande> commandes = commandeRepository.findAll();

        for (Commande cmd : commandes) {
            for (LigneCommande ligne : cmd.getLignes()) {
                Categorie cat = ligne.getProduit().getCategorie();
                double total = ligne.getSousTotal();
                totals.put(cat, totals.getOrDefault(cat, 0.0) + total);
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

    // TOP 3

    @Override
    public List<CategoryStatsDTO> top3Categories() {
        return bestCategories()
                .stream()
                .limit(3)
                .collect(Collectors.toList());
    }


    // nombre de compagnie active (i.e: qui ont soit passé au moins une commande soit vendu au moins un produit

    @Override
    public int nombreCompaniesActives() {

        Set<Company> actives = new HashSet<>();
        List<Commande> commandes = commandeRepository.findAll();

        for (Commande cmd : commandes) {

            actives.add(cmd.getCompany());

            for (LigneCommande ligne : cmd.getLignes()) {
                actives.add(ligne.getProduit().getCompany());
            }
        }

        return actives.size();
    }
}
