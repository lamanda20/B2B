package com.b2b.config;

import com.b2b.model.Categorie;
import com.b2b.repository.CategorieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CategorySeeder implements CommandLineRunner {

    private final CategorieRepository categorieRepo;

    @Override
    public void run(String... args) {

        if (categorieRepo.count() > 0) return;

        System.out.println(">>> Seeding Categories...");

        categorieRepo.saveAll(List.of(
                new Categorie(null, "Heavy Machinery", "Large industrial equipment"),
                new Categorie(null, "Construction Materials", "Building supplies and aggregates"),
                new Categorie(null, "Electrical Equipment", "Industrial electrical components"),
                new Categorie(null, "Industrial Safety", "Protection and PPE equipment"),
                new Categorie(null, "Tools & Hardware", "Professional tools"),
                new Categorie(null, "Plumbing & HVAC", "Heating, ventilation, and plumbing")
        ));

    }
}
