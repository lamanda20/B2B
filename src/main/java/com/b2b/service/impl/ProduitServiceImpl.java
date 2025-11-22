package com.b2b.service.impl;

import com.b2b.model.Produit;
import com.b2b.repository.ProduitRepository;
import com.b2b.service.ProduitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProduitServiceImpl implements ProduitService {

    private final ProduitRepository produitRepository;

    @Autowired
    public ProduitServiceImpl(ProduitRepository produitRepository) {
        this.produitRepository = produitRepository;
    }

    @Override
    public List<Produit> findAll() {
        return produitRepository.findAll();
    }

    @Override
    public Optional<Produit> findById(Long id) {
        return produitRepository.findById(id);
    }

    @Override
    public Produit create(Produit produit) {
        return produitRepository.save(produit);
    }

    @Override
    public Produit update(Long id, Produit produitDetails) {
        return produitRepository.findById(id)
                .map(produit -> {
                    produit.setName(produitDetails.getName());
                    produit.setDescription(produitDetails.getDescription());
                    produit.setPrice(produitDetails.getPrice());
                    produit.setStock(produitDetails.getStock());
                    if (produitDetails.getCategorie() != null) {
                        produit.setCategorie(produitDetails.getCategorie());
                    }
                    return produitRepository.save(produit);
                })
                .orElseThrow(() -> new RuntimeException("Produit non trouvé avec l'id: " + id));
    }

    @Override
    public void delete(Long id) {
        produitRepository.deleteById(id);
    }

    @Override
    public List<Produit> findByCompany(Long companyId) {
        return produitRepository.findByCompanyId(companyId);
    }

    @Override
    public List<Produit> findByCategorie(Integer categorieId) {
        return produitRepository.findByCategorieIdCat(categorieId);
    }

    @Override
    public List<Produit> searchByName(String name) {
        return produitRepository.findByNameContainingIgnoreCase(name);
    }

    @Override
    public List<Produit> findInStock() {
        return produitRepository.findByStockGreaterThan(0);
    }
}
