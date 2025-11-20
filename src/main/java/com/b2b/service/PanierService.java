package com.b2b.service;

import com.b2b.model.Panier;
import com.b2b.model.Produit;
import com.b2b.model.Company;
import com.b2b.model.Commande;

public interface PanierService {
    Panier getPanierByCompany(Company company);
    Panier ajouterProduit(Long companyId, Long produitId, int quantite);
    Panier getPanierByCompanyId(Long companyId);
    Panier supprimerProduit(Long companyId, Long produitId);
    Commande validerCommande(Long companyId);
}
