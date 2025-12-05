package com.b2b.config;

import com.b2b.model.Produit;
import com.b2b.model.Review;
import com.b2b.repository.ProduitRepository;
import com.b2b.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Component
@RequiredArgsConstructor
public class ReviewSeeder implements CommandLineRunner {

    private final ProduitRepository produitRepo;
    private final ReviewRepository reviewRepo;

    @Override
    public void run(String... args) {

        List<Produit> products = produitRepo.findAll();
        Random random = new Random();

        for (Produit p : products) {

            long count = reviewRepo.countByProduct(p);
            if (count > 0) continue;  // already has reviews, skip

            Review r1 = new Review(
                    null, p, null,
                    p.getCompany() != null ? p.getCompany().getName() : "BTP & Co",
                    5,
                    "Excellente qualité, idéal pour nos chantiers.",
                    LocalDateTime.now().minusDays(1)
            );

            Review r2 = new Review(
                    null, p, null,
                    "AtlasSteel",
                    4,
                    "Très bon rapport qualité/prix.",
                    LocalDateTime.now().minusDays(3)
            );

            Review r3 = new Review(
                    null, p, null,
                    "MarocMetal",
                    4 + random.nextInt(2), // 4 or 5
                    "Livraison rapide, produit conforme.",
                    LocalDateTime.now().minusDays(5)
            );

            reviewRepo.saveAll(List.of(r1, r2, r3));
        }
    }
}
