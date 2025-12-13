package com.b2b.service.impl;

import com.b2b.dto.CategoryDto;
import com.b2b.model.Categorie;
import com.b2b.model.Produit;
import com.b2b.repository.CategorieRepository;
import com.b2b.repository.ProduitRepository;
import com.b2b.service.CategorieService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class CategorieServiceImpl implements CategorieService {

    private final CategorieRepository categorieRepository;
    private final ProduitRepository produitRepository;

    private static final Map<Integer, List<String>> CATEGORY_ATTRIBUTES = Map.of(
            1, List.of("brand", "power", "weight"),     // Heavy equipment
            2, List.of("brand", "material"),            // Building materials
            3, List.of("brand", "voltage", "phase"),    // Electrical
            4, List.of("brand", "norm"),                // PPE
            5, List.of("brand", "type"),                // Tools
            6, List.of("brand", "diameter")             // Plumbing
    );

    private List<String> getAttributesForCategory(Integer categoryId) {
        return CATEGORY_ATTRIBUTES.getOrDefault(categoryId, List.of("brand"));
    }

    @Override
    public List<Categorie> findAll() {
        return categorieRepository.findAll();
    }

    @Override
    public Optional<Categorie> findById(Integer id) {
        return categorieRepository.findById(id);
    }

    @Override
    public Optional<Categorie> findByName(String name) {
        return categorieRepository.findByName(name);
    }

    @Override
    public Categorie create(CategoryDto dto) {
        if (categorieRepository.existsByName(dto.name())) {
            throw new RuntimeException("Une catégorie avec ce nom existe déjà");
        }

        Categorie c = new Categorie();
        c.setName(dto.name());
        c.setDescription(dto.description());
        return categorieRepository.save(c);
    }

    @Override
    public Categorie update(Integer id, CategoryDto dto) {
        return categorieRepository.findById(id)
                .map(c -> {
                    c.setName(dto.name());
                    c.setDescription(dto.description());
                    return categorieRepository.save(c);
                })
                .orElseThrow(() -> new RuntimeException("Catégorie non trouvée"));
    }

    @Override
    public void delete(Integer id) {
        categorieRepository.deleteById(id);
    }

    /**
     * CORE FILTER LOGIC
     * Reads filterTag = "key=value;key=value"
     * Groups values per key
     */
    @Override
    public Map<String, List<String>> getFiltersForCategory(Integer id) {

        List<Produit> products = produitRepository.findByCategorieIdCat(id);
        List<String> allowedAttrs = getAttributesForCategory(id);

        Map<String, List<String>> result = new HashMap<>();
        for (String attr : allowedAttrs) {
            result.put(attr, new java.util.ArrayList<>());
        }

        for (Produit p : products) {
            if (p.getFilterTag() == null || p.getFilterTag().isBlank()) continue;

            String[] pairs = p.getFilterTag().split(";");
            for (String pair : pairs) {
                String[] kv = pair.split("=");
                if (kv.length != 2) continue;

                String key = kv[0].trim();
                String value = kv[1].trim();

                if (!result.containsKey(key)) continue;

                result.get(key).add(value);
            }
        }

        // remove duplicates
        result.replaceAll((k, v) -> v.stream().distinct().toList());

        return result;
    }
}
