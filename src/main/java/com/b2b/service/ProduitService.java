package com.b2b.service;

import com.b2b.dto.ProduitDTO;
import com.b2b.dto.ProduitSearchDTO;
import com.b2b.model.Produit;
import com.b2b.model.Categorie;
import com.b2b.exception.ResourceNotFoundException;
import com.b2b.exception.BusinessException;
import com.b2b.exception.ValidationException;
import com.b2b.repository.ProduitRepository;
import com.b2b.repository.CategorieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProduitService {

    @Autowired
    private ProduitRepository produitRepository;

    @Autowired
    private CategorieRepository categorieRepository;

    // Recherche et filtres avancés
    public List<ProduitDTO> searchProduits(ProduitSearchDTO searchDTO) {
        try {
            // Validation des paramètres
            validateSearchParameters(searchDTO);

            Categorie categorie = null;
            if (searchDTO.getCategorieId() != null) {
                categorie = categorieRepository.findById(searchDTO.getCategorieId())
                        .orElseThrow(() -> new ResourceNotFoundException("Catégorie", "id", searchDTO.getCategorieId()));
            }

            List<Produit> produits = produitRepository.findByAdvancedFilters(
                    searchDTO.getKeyword(),
                    categorie,
                    searchDTO.getMinPrice(),
                    searchDTO.getMaxPrice(),
                    searchDTO.getMinStock(),
                    null // companyId
            );

            // Appliquer le tri si spécifié
            if (searchDTO.getSortBy() != null) {
                produits = sortProduits(produits, searchDTO.getSortBy(), searchDTO.getSortDirection());
            }

            return produits.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());

        } catch (ResourceNotFoundException e) {
            throw e; // Relancer les exceptions spécifiques
        } catch (Exception e) {
            throw new BusinessException("Erreur lors de la recherche des produits: " + e.getMessage(), e);
        }
    }

    // Recherche simple par mot-clé
    public List<ProduitDTO> findAll(String keyword) {
        try {
            List<Produit> produits;

            if (keyword == null || keyword.trim().isEmpty()) {
                produits = produitRepository.findAll();
            } else {
                produits = produitRepository.findByKeyword(keyword);
            }

            if (produits.isEmpty()) {
                throw new ResourceNotFoundException("Aucun produit trouvé" +
                        (keyword != null ? " pour la recherche: " + keyword : ""));
            }

            return produits.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());

        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException("Erreur lors de la récupération des produits", e);
        }
    }

    // Trouver par ID
    public ProduitDTO findById(Long id) {
        try {
            if (id == null || id <= 0) {
                throw new ValidationException("id", "L'ID doit être positif");
            }

            Produit produit = produitRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Produit", "id", id));
            return convertToDTO(produit);

        } catch (ResourceNotFoundException | ValidationException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException("Erreur lors de la récupération du produit ID: " + id, e);
        }
    }

    // Filtrer par catégorie
    public List<ProduitDTO> findByCategorie(Long categorieId) {
        try {
            if (categorieId == null || categorieId <= 0) {
                throw new ValidationException("categorieId", "L'ID de catégorie doit être positif");
            }

            Categorie categorie = categorieRepository.findById(categorieId)
                    .orElseThrow(() -> new ResourceNotFoundException("Catégorie", "id", categorieId));

            List<Produit> produits = produitRepository.findByCategorie(categorie);

            if (produits.isEmpty()) {
                throw new ResourceNotFoundException("Aucun produit trouvé pour la catégorie ID: " + categorieId);
            }

            return produits.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());

        } catch (ResourceNotFoundException | ValidationException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException("Erreur lors du filtrage par catégorie", e);
        }
    }

    // Filtrer par prix
    public List<ProduitDTO> findByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        try {
            validatePriceRange(minPrice, maxPrice);

            List<Produit> produits = produitRepository.findByPriceBetween(minPrice, maxPrice);

            if (produits.isEmpty()) {
                throw new ResourceNotFoundException(
                        String.format("Aucun produit trouvé dans la fourchette de prix %s - %s", minPrice, maxPrice));
            }

            return produits.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());

        } catch (ResourceNotFoundException | ValidationException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException("Erreur lors du filtrage par prix", e);
        }
    }

    // Méthodes de validation
    private void validateSearchParameters(ProduitSearchDTO searchDTO) {
        if (searchDTO == null) {
            throw new ValidationException("searchDTO", "Les critères de recherche ne peuvent pas être nuls");
        }

        if (searchDTO.getMinPrice() != null && searchDTO.getMaxPrice() != null) {
            validatePriceRange(searchDTO.getMinPrice(), searchDTO.getMaxPrice());
        }

        if (searchDTO.getMinStock() != null && searchDTO.getMinStock() < 0) {
            throw new ValidationException("minStock", "Le stock minimum ne peut pas être négatif");
        }
    }

    private void validatePriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        if (minPrice == null || maxPrice == null) {
            throw new ValidationException("price", "Les prix min et max doivent être spécifiés");
        }

        if (minPrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new ValidationException("minPrice", "Le prix minimum ne peut pas être négatif");
        }

        if (maxPrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new ValidationException("maxPrice", "Le prix maximum ne peut pas être négatif");
        }

        if (minPrice.compareTo(maxPrice) > 0) {
            throw new ValidationException("price", "Le prix minimum ne peut pas être supérieur au prix maximum");
        }
    }

    // Méthode de tri (inchangée)
    private List<Produit> sortProduits(List<Produit> produits, String sortBy, String direction) {
        boolean ascending = !"desc".equalsIgnoreCase(direction);

        return produits.stream()
                .sorted((p1, p2) -> {
                    int compareResult = 0;
                    switch (sortBy.toLowerCase()) {
                        case "name":
                            compareResult = p1.getName().compareTo(p2.getName());
                            break;
                        case "price":
                            compareResult = p1.getPrice().compareTo(p2.getPrice());
                            break;
                        case "stock":
                            compareResult = p1.getStock().compareTo(p2.getStock());
                            break;
                        default:
                            compareResult = p1.getId().compareTo(p2.getId());
                    }
                    return ascending ? compareResult : -compareResult;
                })
                .collect(Collectors.toList());
    }

    // Conversion Entity → DTO
    private ProduitDTO convertToDTO(Produit produit) {
        ProduitDTO dto = new ProduitDTO();
        dto.setId(produit.getId());
        dto.setName(produit.getName());
        dto.setDescription(produit.getDescription());
        dto.setPrice(produit.getPrice());
        dto.setStock(produit.getStock());
        dto.setCompanyId(produit.getCompanyId());

        if (produit.getCategorie() != null) {
            dto.setCategorieName(produit.getCategorie().getName());
            dto.setCategorieId(produit.getCategorie().getId());
        }

        return dto;
    }
}