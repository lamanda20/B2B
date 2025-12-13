package com.b2b.config;

import com.b2b.model.Categorie;
import com.b2b.model.Company;
import com.b2b.model.Produit;
import com.b2b.repository.CategorieRepository;
import com.b2b.repository.CompanyRepository;
import com.b2b.repository.ProduitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

//@Component
@RequiredArgsConstructor
public class ProductSeeder implements CommandLineRunner {

    private final ProduitRepository produitRepo;
    private final CategorieRepository categorieRepo;
    private final CompanyRepository companyRepo;

    @Override
    public void run(String... args) {

        if (produitRepo.count() > 0) return;

        System.out.println(">>> Seeding Products...");

        Company c = companyRepo.findAll().stream().findFirst().orElse(null);
        if (c == null) {
            System.out.println("!!! No company found, skipping product seed.");
            return;
        }

        List<Categorie> categories = categorieRepo.findAll();
        if (categories.size() < 6) {
            System.out.println("!!! Categories missing, skipping product seed.");
            return;
        }

        java.util.function.Function<Integer, Categorie> cat =
                id -> categories.stream()
                        .filter(x -> x.getIdCat().equals(id))
                        .findFirst()
                        .orElse(null);

        /* =====================================================
           1) HEAVY MACHINERY
           attrs: brand, power, weight
           ===================================================== */
        produitRepo.saveAll(List.of(
                new Produit(null, "excavator.jpg",
                        "Caterpillar Excavator 320D",
                        "Heavy-duty diesel excavator",
                        320000, 4, c, cat.apply(1),
                        "brand=Caterpillar;power=200HP;weight=21000kg"),

                new Produit(null, "bulldozer.jpg",
                        "Komatsu Bulldozer D85",
                        "Large bulldozer",
                        475000, 3, c, cat.apply(1),
                        "brand=Komatsu;power=305HP;weight=28000kg"),

                new Produit(null, "wheel_loader.jpg",
                        "Volvo Wheel Loader L120H",
                        "Construction loader",
                        410000, 6, c, cat.apply(1),
                        "brand=Volvo;power=271HP;weight=20000kg")
        ));

        /* =====================================================
           2) CONSTRUCTION MATERIALS
           attrs: brand, material
           ===================================================== */
        produitRepo.saveAll(List.of(
                new Produit(null, "cement.jpg",
                        "Cement 50kg Premium",
                        "High-grade cement",
                        75, 200, c, cat.apply(2),
                        "brand=Lafarge;material=Cement"),

                new Produit(null, "rebar.jpg",
                        "Steel Rebar 12mm",
                        "High strength steel",
                        42, 500, c, cat.apply(2),
                        "brand=MaghrebSteel;material=Steel")
        ));

        /* =====================================================
           3) ELECTRICAL EQUIPMENT
           attrs: brand, voltage, phase
           ===================================================== */
        produitRepo.saveAll(List.of(
                new Produit(null, "cable_380.jpg",
                        "Industrial Cable 380V",
                        "Armored industrial cable",
                        25, 350, c, cat.apply(3),
                        "brand=Nexans;voltage=380V;phase=3-phase"),

                new Produit(null, "transformer.jpg",
                        "Transformer 5kVA",
                        "Single phase transformer",
                        600, 20, c, cat.apply(3),
                        "brand=Schneider;voltage=220V;phase=1-phase")
        ));

        /* =====================================================
           4) INDUSTRIAL SAFETY (PPE)
           attrs: brand, norm
           ===================================================== */
        produitRepo.saveAll(List.of(
                new Produit(null, "helmet.jpg",
                        "Safety Helmet",
                        "Impact resistant helmet",
                        60, 250, c, cat.apply(4),
                        "brand=DeltaPlus;norm=EN397"),

                new Produit(null, "boots.jpg",
                        "Safety Boots",
                        "Steel toe boots",
                        140, 120, c, cat.apply(4),
                        "brand=Uvex;norm=EN20345")
        ));

        /* =====================================================
           5) TOOLS & HARDWARE
           attrs: brand, type
           ===================================================== */
        produitRepo.saveAll(List.of(
                new Produit(null, "drill.jpg",
                        "Bosch Drill 750W",
                        "Impact drill",
                        950, 50, c, cat.apply(5),
                        "brand=Bosch;type=Drill"),

                new Produit(null, "grinder.jpg",
                        "Makita Grinder",
                        "Cutting tool",
                        680, 40, c, cat.apply(5),
                        "brand=Makita;type=Grinder")
        ));

        /* =====================================================
           6) PLUMBING & HVAC
           attrs: brand, diameter
           ===================================================== */
        produitRepo.saveAll(List.of(
                new Produit(null, "pipe20.jpg",
                        "PVC Pipe 20mm",
                        "High pressure pipe",
                        22, 300, c, cat.apply(6),
                        "brand=Wavin;diameter=20mm"),

                new Produit(null, "pump.jpg",
                        "Water Pump 1HP",
                        "Domestic water pump",
                        850, 25, c, cat.apply(6),
                        "brand=Grundfos;diameter=1inch")
        ));

        System.out.println(">>> Products OK (" + produitRepo.count() + ")");
    }
}
