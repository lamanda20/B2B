package com.b2b.service.impl;

import com.b2b.dto.CategoryDto;
import com.b2b.model.Categorie;
import com.b2b.repository.CategorieRepository;
import com.b2b.service.CategorieService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class CategorieServiceImpl implements CategorieService {

    private final CategorieRepository categorieRepository;

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

    @Override
    public Map<String, List<String>> getFiltersForCategory(Integer id) {

        return switch (id) {

            // 1) Heavy Machinery
            case 1 -> Map.of(
                    "brand", List.of("Caterpillar", "Komatsu", "Volvo", "Manitou", "HAMM"),
                    "power", List.of("100 kW", "200 kW", "300 kW")
            );

            // 2) Construction Materials
            case 2 -> Map.of(
                    "material", List.of("Cement", "Sand", "Gravel", "Steel", "Blocks", "Tiles"),
                    "grade", List.of("Standard", "Premium")
            );

            // 3) Electrical Equipment
            case 3 -> Map.of(
                    "voltage", List.of("220V", "380V"),
                    "phase", List.of("Single Phase", "Three Phase")
            );

            // 4) Industrial Safety
            case 4 -> Map.of(
                    "size", List.of("S", "M", "L", "XL"),
                    "certification", List.of("EN-397", "Class 2", "S3")
            );

            // 5) Tools & Hardware
            case 5 -> Map.of(
                    "brand", List.of("Bosch", "Makita"),
                    "power", List.of("500W", "750W", "1000W")
            );

            // 6) Plumbing & HVAC
            case 6 -> Map.of(
                    "diameter", List.of("20mm", "40mm", "1 inch"),
                    "type", List.of("Pipe", "Valve", "Pump", "Filter")
            );

            default -> Map.of(); // no filters
        };
    }

}
