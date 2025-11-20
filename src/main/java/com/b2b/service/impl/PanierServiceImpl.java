package com.b2b.service.impl;

import com.b2b.model.*;
import com.b2b.repository.*;
import com.b2b.service.PanierService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PanierServiceImpl implements PanierService {

    private final PanierRepository panierRepository;
    private final ProduitRepository produitRepository;
    private final CompanyRepository companyRepository;
    private final CommandeRepository commandeRepository;

    public PanierServiceImpl(PanierRepository panierRepository,
                             ProduitRepository produitRepository,
                             CompanyRepository companyRepository,
                             CommandeRepository commandeRepository) {
        this.panierRepository = panierRepository;
        this.produitRepository = produitRepository;
        this.companyRepository = companyRepository;
        this.commandeRepository = commandeRepository;
    }

    @Override
    public Panier getPanierByCompany(Company company) {
        return panierRepository.findByCompany(company);
    }

    @Override
    public Panier ajouterProduit(Long companyId, Long produitId, int quantite) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new RuntimeException("Company not found"));

        Panier panier = panierRepository.findByCompany(company);
        if (panier == null) {
            panier = new Panier();
            panier.setCompany(company);
        }

        Produit produit = produitRepository.findById(produitId)
                .orElseThrow(() -> new RuntimeException("Produit non trouvé"));
        panier.ajouterProduit(produit, quantite);

        return panierRepository.save(panier);
    }

    @Override
    public Panier supprimerProduit(Long companyId, Long produitId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new RuntimeException("Company not found"));

        Panier panier = panierRepository.findByCompany(company);
        if (panier == null) {
            throw new RuntimeException("Panier vide");
        }

        Produit produit = produitRepository.findById(produitId)
                .orElseThrow(() -> new RuntimeException("Produit non trouvé"));

        panier.supprimerProduit(produit);

        return panierRepository.save(panier);
    }
    @Override
    public Panier getPanierByCompanyId(Long companyId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new RuntimeException("Company not found"));

        return panierRepository.findByCompany(company);
    }
    @Override
    public Commande validerCommande(Long companyId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new RuntimeException("Company not found"));

        Panier panier = panierRepository.findByCompany(company);
        if (panier == null || panier.getLignes().isEmpty()) {
            throw new RuntimeException("Panier vide");
        }

        Commande commande = panier.validerCommande();
        commande.setCompany(company);

        return commandeRepository.save(commande);
    }
}
