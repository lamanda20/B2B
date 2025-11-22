package com.b2b.config;

import com.b2b.model.Categorie;
import com.b2b.model.Produit;
import com.b2b.repository.CategorieRepository;
import com.b2b.repository.ProduitRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder {

    private final CategorieRepository catRepo;
    private final ProduitRepository prodRepo;

    public DataSeeder(CategorieRepository catRepo, ProduitRepository prodRepo) {
        this.catRepo = catRepo;
        this.prodRepo = prodRepo;
    }

    @PostConstruct
    public void seed() {

        if (catRepo.count() > 0) return; // avoid duplication

        // ================================================
        // 1) CREATE CATEGORIES
        // ================================================
        Categorie cat1 = makeCat("Heavy Machinery", "Industrial heavy-duty equipment");
        Categorie cat2 = makeCat("Construction Materials", "Raw materials for building and civil engineering");
        Categorie cat3 = makeCat("Electrical Equipment", "Cables, wiring, voltage systems");
        Categorie cat4 = makeCat("Industrial Safety", "Protective gear and workplace safety");
        Categorie cat5 = makeCat("Tools & Hardware", "Professional tools and accessories");
        Categorie cat6 = makeCat("Plumbing & HVAC", "Pipes, valves, heating & cooling systems");


        // ================================================
        // 2) ADD PRODUCTS PER CATEGORY
        // ================================================
        // HEAVY MACHINERY
        saveProd(cat1, "Mini Excavator HX20", "Compact excavator for small sites", 980000, 5,
                "/images/products/excavator.png");
        saveProd(cat1, "Bulldozer D5K2", "High-power dozer blade", 1200000, 3,
                "/images/products/bulldozer.png");
        saveProd(cat1, "Wheel Loader 966H", "Heavy loader for quarries", 2100000, 2,
                "/images/products/loader.png");
        saveProd(cat1, "Skid Steer 272D3", "Universal construction vehicle", 650000, 7,
                "/images/products/skidsteer.png");
        saveProd(cat1, "Forklift H150", "Industrial forklift 15 tons", 400000, 4,
                "/images/products/forklift.png");
        saveProd(cat1, "Compact Roller CR30", "Road compactor 3 tons", 310000, 6,
                "/images/products/roller.png");


        // CONSTRUCTION MATERIALS
        saveProd(cat2, "Cement 50kg – CEM II", "High-grade cement for construction", 89.99, 500,
                "/images/products/cement.png");
        saveProd(cat2, "Rebar Steel Ø12", "Steel reinforcement bars", 24.50, 1200,
                "/images/products/rebar.png");
        saveProd(cat2, "Concrete Blocks 20x40", "Premium blocks for building", 6.30, 4000,
                "/images/products/concreteblock.png");
        saveProd(cat2, "Sand 25kg Bag", "High-purity washed sand", 15.99, 900,
                "/images/products/sand.png");
        saveProd(cat2, "Gravel 50kg", "Crushed stone for foundations", 39.90, 300,
                "/images/products/gravel.png");
        saveProd(cat2, "Plaster Board 2m", "Gypsum sheets for interiors", 55, 250,
                "/images/products/plasterboard.png");


        // ELECTRICAL EQUIPMENT
        saveProd(cat3, "Cable 380V", "Industrial high-voltage cable", 149.99, 120,
                "/images/products/cable380.png");
        saveProd(cat3, "Copper Cable 50mm²", "High-conductivity copper wiring", 89.99, 80,
                "/images/products/coppercable.png");
        saveProd(cat3, "Transformer 50kVA", "Voltage transformer", 25000, 10,
                "/images/products/transformer.png");
        saveProd(cat3, "Industrial LED Floodlight", "High-power site LED", 799, 40,
                "/images/products/floodlight.png");
        saveProd(cat3, "Electrical Panel 18-Modules", "Ready-to-install panel", 650, 35,
                "/images/products/electricalpanel.png");
        saveProd(cat3, "Circuit Breaker 32A", "Industrial over-current breaker", 120, 140,
                "/images/products/breaker.png");


        // INDUSTRIAL SAFETY
        saveProd(cat4, "Safety Helmet ProGuard", "Shock-resistant helmet", 129, 200,
                "/images/products/helmet.png");
        saveProd(cat4, "Safety Boots SteelCap", "Steel-toe boots", 399, 150,
                "/images/products/boots.png");
        saveProd(cat4, "High-Visibility Vest", "EN20471 reflective vest", 49, 350,
                "/images/products/vest.png");
        saveProd(cat4, "N95 Respirator Mask", "Industrial dust mask", 9.99, 2000,
                "/images/products/mask.png");
        saveProd(cat4, "Protective Gloves MAX-GRIP", "Chemical-resistant gloves", 29, 500,
                "/images/products/gloves.png");
        saveProd(cat4, "Ear Protection HPX500", "Noise-cancel ear defenders", 199, 120,
                "/images/products/earprotection.png");


        // TOOLS & HARDWARE
        saveProd(cat5, "Hammer Drill 1200W", "Powerful SDS+ hammer drill", 799, 70,
                "/images/products/drill.png");
        saveProd(cat5, "Angle Grinder 230mm", "Heavy-duty rotary grinder", 699, 65,
                "/images/products/grinder.png");
        saveProd(cat5, "Professional Screwdriver Set", "42-piece magnetic set", 129, 300,
                "/images/products/screwdriverset.png");
        saveProd(cat5, "Steel Hammer 1kg", "Tempered steel hammer", 39, 500,
                "/images/products/hammer.png");
        saveProd(cat5, "Laser Distance Meter 60m", "Digital laser measurer", 249, 90,
                "/images/products/lasermeter.png");
        saveProd(cat5, "Adjustable Wrench 12in", "Forged steel wrench", 69, 400,
                "/images/products/wrench.png");


        // PLUMBING & HVAC
        saveProd(cat6, "PVC Pipe Ø50", "High-pressure PVC pipe", 19.99, 850,
                "/images/products/pvcpipes.png");
        saveProd(cat6, "Copper Pipe 1m", "Thermal-resistant pipe", 49.99, 600,
                "/images/products/copperpipe.png");
        saveProd(cat6, "Ball Valve 1in", "Industrial metal valve", 39, 300,
                "/images/products/valve.png");
        saveProd(cat6, "Water Pump 1.5HP", "High-flow domestic pump", 1290, 40,
                "/images/products/pump.png");
        saveProd(cat6, "Radiator 1200W", "Electric wall radiator", 899, 30,
                "/images/products/radiator.png");
        saveProd(cat6, "Air Ventilation Duct", "Galvanized ventilation pipe", 149, 100,
                "/images/products/vent.png");
    }


    // ================================================
    // HELPERS
    // ================================================
    private Categorie makeCat(String name, String desc) {
        Categorie c = new Categorie();
        c.setName(name);
        c.setDescription(desc);
        return catRepo.save(c);
    }

    private void saveProd(Categorie cat, String name, String desc, double price, int stock, String imgUrl) {
        Produit p = new Produit();
        p.setName(name);
        p.setDescription(desc);
        p.setPrice(price);
        p.setStock(stock);
        p.setCategorie(cat);
        p.setImageUrl(imgUrl);
        prodRepo.save(p);
    }
}
