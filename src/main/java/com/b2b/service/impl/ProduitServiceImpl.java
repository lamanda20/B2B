package com.b2b.service.impl;

import com.b2b.dto.ProductDto;
import com.b2b.model.Categorie;
import com.b2b.model.Company;
import com.b2b.model.Produit;
import com.b2b.repository.CategorieRepository;
import com.b2b.repository.CompanyRepository;
import com.b2b.repository.ProduitRepository;
import com.b2b.service.ProduitService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class ProduitServiceImpl implements ProduitService {

    private final ProduitRepository produitRepository;
    private final CategorieRepository categorieRepository;
    private final CompanyRepository companyRepository;

    // ===========================================
    // FIND
    // ===========================================
    @Override
    public List<Produit> findAll() {
        return produitRepository.findAll();
    }

    @Override
    public Optional<Produit> findById(Long id) {
        return produitRepository.findById(id);
    }

    // ===========================================
    // CREATE
    // ===========================================
    @Override
    public Produit create(ProductDto dto) {
        Produit p = new Produit();
        copyDtoToEntity(dto, p);
        return produitRepository.save(p);
    }

    // ===========================================
    // UPDATE
    // ===========================================
    @Override
    public Produit update(Long id, ProductDto dto) {
        return produitRepository.findById(id)
                .map(existing -> {
                    copyDtoToEntity(dto, existing);
                    return produitRepository.save(existing);
                })
                .orElseThrow(() -> new RuntimeException("Produit non trouvé"));
    }

    private void copyDtoToEntity(ProductDto dto, Produit p) {
        p.setName(dto.getName());
        p.setDescription(dto.getDescription());
        p.setPrice(dto.getPrice());
        p.setStock(dto.getStock());
        p.setImageUrl(dto.getImageUrl());
        p.setFilterTag(dto.getFilterTag());

        if (dto.getCategoryId() != null) {
            Categorie c = categorieRepository.findById(dto.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Catégorie invalide"));
            p.setCategorie(c);
        }

        if (dto.getCompanyId() != null) {
            Company cmp = companyRepository.findById(dto.getCompanyId())
                    .orElseThrow(() -> new RuntimeException("Entreprise invalide"));
            p.setCompany(cmp);
        }
    }

    // ===========================================
    // DELETE
    // ===========================================
    @Override
    public void delete(Long id) {
        if (!produitRepository.existsById(id)) {
            throw new RuntimeException("Produit non trouvé");
        }

        try {
            produitRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Impossible de supprimer : le produit est utilisé dans une commande.");
        }
    }


    // ===========================================
    // FILTERS
    // ===========================================
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

    @Override
    public List<Produit> filter(int categoryId, Map<String, String> filters) {

        if (filters == null || filters.isEmpty()) {
            return produitRepository.findByCategorieIdCat(categoryId);
        }

        return produitRepository.findByCategorieIdCat(categoryId)
                .stream()
                .filter(p -> {
                    if (p.getFilterTag() == null) return false;

                    String tag = p.getFilterTag().toLowerCase();

                    // AND logic: all filters must match
                    for (Map.Entry<String, String> entry : filters.entrySet()) {
                        String expected = entry.getKey().toLowerCase() + "="
                                + entry.getValue().toLowerCase();
                        if (!tag.contains(expected)) {
                            return false;
                        }
                    }
                    return true;
                })
                .toList();
    }

}
