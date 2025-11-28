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

@Component
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

        // Helper
        java.util.function.Function<Integer, Categorie> cat = (id) ->
                categories.stream()
                        .filter(x -> x.getIdCat().equals(id))
                        .findFirst()
                        .orElse(null);

        // ===========================
        // 1) Heavy Machinery
        // ===========================
        produitRepo.saveAll(List.of(
                new Produit(null, "excavator.jpg", "Caterpillar Excavator 320D", "Heavy-duty diesel excavator", 320000, 4, c, cat.apply(1), "Caterpillar"),
                new Produit(null, "bulldozer.jpg", "Komatsu Bulldozer D85", "Large bulldozer", 475000, 3, c, cat.apply(1), "Komatsu"),
                new Produit(null, "wheel_loader.jpg", "Volvo Wheel Loader L120H", "Construction loader", 410000, 6, c, cat.apply(1), "Volvo"),
                new Produit(null, "mini_excavator.jpg", "Kubota Mini Excavator", "Urban compact excavator", 125000, 8, c, cat.apply(1), "Kubota"),
                new Produit(null, "telehandler.jpg", "Manitou Telehandler MT-X", "High reach", 150000, 5, c, cat.apply(1), "Manitou"),
                new Produit(null, "roller.jpg", "HAMM Road Roller HD14", "Vibratory roller", 92000, 7, c, cat.apply(1), "HAMM")
        ));

        // ===========================
        // 2) Construction Materials
        // ===========================
        produitRepo.saveAll(List.of(
                new Produit(null, "cement.jpg", "Cement 50kg Premium", "High-grade cement", 75, 200, c, cat.apply(2), "Premium"),
                new Produit(null, "sand.jpg", "Sand 25kg", "Washed clean sand", 30, 350, c, cat.apply(2), "Standard"),
                new Produit(null, "gravel.jpg", "Gravel 50kg", "Construction gravel", 55, 180, c, cat.apply(2), "Standard"),
                new Produit(null, "rebar.jpg", "Steel Rebar 12mm", "High strength steel", 42, 500, c, cat.apply(2), "Steel"),
                new Produit(null, "blocks.jpg", "Concrete Blocks", "Hollow blocks", 9, 1000, c, cat.apply(2), "Standard"),
                new Produit(null, "tiles.jpg", "Ceramic Tiles 60x60", "Premium flooring", 120, 300, c, cat.apply(2), "Premium")
        ));

        // ===========================
        // 3) Electrical Equipment
        // ===========================
        produitRepo.saveAll(List.of(
                new Produit(null, "cable_220.jpg", "Cable 220V Copper", "Electrical cable", 12, 500, c, cat.apply(3), "220V"),
                new Produit(null, "cable_380.jpg", "Cable 380V Armored", "Industrial cable", 25, 350, c, cat.apply(3), "380V"),
                new Produit(null, "breaker.jpg", "Circuit Breaker 32A", "Thermal breaker", 90, 100, c, cat.apply(3), "32A"),
                new Produit(null, "transformer.jpg", "Transformer 5kVA", "Single phase", 600, 20, c, cat.apply(3), "5kVA"),
                new Produit(null, "led.jpg", "LED Floodlight 200W", "IP65 LED", 150, 70, c, cat.apply(3), "200W"),
                new Produit(null, "panel.jpg", "Industrial Socket Panel", "3-phase", 180, 40, c, cat.apply(3), "3-phase")
        ));

        // ===========================
        // 4) Industrial Safety
        // ===========================
        produitRepo.saveAll(List.of(
                new Produit(null, "helmet.jpg", "Safety Helmet", "Impact resistant", 60, 250, c, cat.apply(4), "Helmet"),
                new Produit(null, "gloves.jpg", "Heat Gloves", "High temperature", 35, 400, c, cat.apply(4), "Gloves"),
                new Produit(null, "boots.jpg", "Safety Boots", "Steel toe", 140, 120, c, cat.apply(4), "Boots"),
                new Produit(null, "goggles.jpg", "Dust Goggles", "Anti fog", 45, 220, c, cat.apply(4), "Goggles"),
                new Produit(null, "vest.jpg", "Reflective Vest", "High visibility", 30, 300, c, cat.apply(4), "Vest"),
                new Produit(null, "mask.jpg", "Respirator", "Dual filter", 80, 150, c, cat.apply(4), "Mask")
        ));

        // ===========================
        // 5) Tools & Hardware
        // ===========================
        produitRepo.saveAll(List.of(
                new Produit(null, "drill.jpg", "Bosch Drill 750W", "Impact drill", 950, 50, c, cat.apply(5), "750W"),
                new Produit(null, "grinder.jpg", "Makita Grinder", "Cutting tool", 680, 40, c, cat.apply(5), "Makita"),
                new Produit(null, "hammer.jpg", "Steel Hammer", "800g hammer", 45, 200, c, cat.apply(5), "Bosch"),
                new Produit(null, "pliers.jpg", "Insulated Pliers", "1000V pliers", 70, 150, c, cat.apply(5), "1000W"),
                new Produit(null, "wrench.jpg", "Adjustable Wrench", "12 inch steel", 65, 120, c, cat.apply(5), "500W"),
                new Produit(null, "screwdriver.jpg", "Screwdriver Set", "Magnetic 12pcs", 85, 160, c, cat.apply(5), "Makita")
        ));

        // ===========================
        // 6) Plumbing & HVAC
        // ===========================
        produitRepo.saveAll(List.of(
                new Produit(null, "pipe20.jpg", "PVC Pipe 20mm", "High pressure pipe", 22, 300, c, cat.apply(6), "20mm"),
                new Produit(null, "pipe40.jpg", "PVC Pipe 40mm", "Drainage pipe", 35, 200, c, cat.apply(6), "40mm"),
                new Produit(null, "valve.jpg", "Brass Valve 1\"", "Anti corrosion", 90, 150, c, cat.apply(6), "Valve"),
                new Produit(null, "filter.jpg", "HVAC Filter", "Air filter", 80, 100, c, cat.apply(6), "Filter"),
                new Produit(null, "coil.jpg", "Fan Coil Unit 2kW", "Heating + cooling", 950, 15, c, cat.apply(6), "Fan Coil"),
                new Produit(null, "pump.jpg", "Water Pump 1HP", "Domestic pump", 850, 25, c, cat.apply(6), "Pump")
        ));

        System.out.println(">>> Products OK (" + produitRepo.count() + ")");
    }
}
