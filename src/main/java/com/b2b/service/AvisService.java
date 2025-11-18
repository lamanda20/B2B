package com.b2b.service;

import com.b2b.model.Avis;
import com.b2b.model.Company;
import com.b2b.model.Company;
import com.b2b.model.Produit;

import java.util.List;
import java.util.Optional;

public interface AvisService {
    List<Avis> findAll();
    Optional<Avis> findById(Long id);
    List<Avis> findByProduit(Long produitId);
    List<Avis> findByCompany(Long CompanyId);
    Avis ajouterAvis(Produit produit, Company company, String feedback, int note);
    void supprimerAvis(Long avisId);
    Double getAverageEvaluation(Long produitId);
}
