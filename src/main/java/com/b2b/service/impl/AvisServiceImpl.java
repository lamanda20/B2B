package com.b2b.service.impl;

import com.b2b.model.Avis;
import com.b2b.model.Company;
import com.b2b.model.Produit;
import com.b2b.repository.AvisRepository;
import com.b2b.service.AvisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AvisServiceImpl implements AvisService {

    private final AvisRepository avisRepository;

    @Autowired
    public AvisServiceImpl(AvisRepository avisRepository) {
        this.avisRepository = avisRepository;
    }

    @Override
    public List<Avis> findAll() {
        return avisRepository.findAll();
    }

    @Override
    public Optional<Avis> findById(Long id) {
        return avisRepository.findById(id);
    }

    @Override
    public List<Avis> findByProduit(Long produitId) {
        return avisRepository.findByProduitId(produitId);
    }

    @Override
    public List<Avis> findByCompany(Long CompanyId) {
        return avisRepository.findByCompanyId(CompanyId);
    }

    @Override
    public Avis ajouterAvis(Produit produit, Company Company, String feedback, int note) {
        if (note < 1 || note > 5) {
            throw new IllegalArgumentException("La note doit être entre 1 et 5");
        }
        Avis avis = Avis.ajouterAvis(produit, Company, feedback, note);
        return avisRepository.save(avis);
    }

    @Override
    public void supprimerAvis(Long avisId) {
        avisRepository.deleteById(avisId);
    }

    @Override
    public Double getAverageEvaluation(Long produitId) {
        return avisRepository.findAverageEvaluationByProduitId(produitId);
    }
}
