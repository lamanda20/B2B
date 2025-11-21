package com.b2b.service;

import com.b2b.dto.CategoryStatsDTO;
import com.b2b.dto.CompanyStatsDTO;
import com.b2b.dto.ProductStatsDTO;

import java.util.List;

public interface StatsService {

    List<CompanyStatsDTO> bestCompaniesAcheteuses();  // Montant total dépensé
    List<CompanyStatsDTO> top3CompaniesAcheteuses();

    List<CompanyStatsDTO> bestCompaniesVendeuses(); // Montant total gagné
    List<CompanyStatsDTO> top3CompaniesVendeuses();

    List<ProductStatsDTO> bestProducts(); // Produits les plus rentables
    List<ProductStatsDTO> top3Products();

    List<CategoryStatsDTO> bestCategories();   // Catégories les plus rentables
    List<CategoryStatsDTO> top3Categories();

    int nombreCompaniesActives(); // Entreprises ayant fait au moins 1 commande
    int nombreProduits();
    int nombreCommadesPasser();







}
