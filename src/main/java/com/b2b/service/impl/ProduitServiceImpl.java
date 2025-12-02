package com.b2b.service.impl;

import com.b2b.dto.ProductDto;
import com.b2b.model.Categorie;
import com.b2b.model.Company;
import com.b2b.model.LigneCommande;
import com.b2b.model.Produit;
import com.b2b.repository.CategorieRepository;
import com.b2b.repository.CompanyRepository;
import com.b2b.repository.LigneCommandeRepository;
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
    private final LigneCommandeRepository ligneCommandeRepository;

    @Override
    public List<Produit> findAll() {
        return produitRepository.findAll();
    }

    @Override
    public Optional<Produit> findById(Long id) {
        return produitRepository.findById(id);
    }

    @Override
    public Produit create(ProductDto dto) {
        Produit p = new Produit();
        copyDtoToEntity(dto, p);
        return produitRepository.save(p);
    }

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

    @Override
    public void delete(Long id) {

        List<LigneCommande> ligneCommandes =  ligneCommandeRepository.findAll();

        for(LigneCommande lc : ligneCommandes){
            System.out.println("wsselt tal hena 1++++++++++++++++ ");

            if(lc.getProduit() != null && lc.getProduit().getId().equals(id)){
                System.out.println("wsselt tal hena 2++++++++++++++++ ");
                lc.setProduit(null);
                ligneCommandeRepository.save(lc);
            }
        }
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


    @Override
    public List<Produit> filter(int categoryId, Map<String, String> filters) {

        // Normalize all filter values
        Map<String, String> normalized = new HashMap<>();
        filters.forEach((k, v) -> normalized.put(k.toLowerCase(), v.trim().toLowerCase()));

        return produitRepository.findByCategorieIdCat(categoryId)
                .stream()
                .filter(p -> {
                    String tag = p.getFilterTag();
                    if (tag == null) return false;

                    String normalizedTag = tag.trim().toLowerCase();

                    // Keep product only if its tag matches ANY of the selected filter values
                    return normalized.values().contains(normalizedTag);
                })
                .toList();
    }


    private boolean matchFilters(Produit p, Map<String, String> filters) {

        for (var entry : filters.entrySet()) {
            String key = entry.getKey();
            String expected = entry.getValue();

            if (expected == null) continue;

            switch (key.toLowerCase()) {
                case "brand":  // brand = filterTag
                    if (p.getFilterTag() == null) return false;
                    if (!p.getFilterTag().equalsIgnoreCase(expected)) return false;
                    break;

                case "power": // in future if you add a power attribute
                    // For now ignore or return true
                    break;

                default:
                    System.out.println("Unknown filter key: " + key);
                    break;
            }
        }

        return true;
    }

}
