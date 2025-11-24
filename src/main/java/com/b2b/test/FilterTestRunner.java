package com.b2b.test;


import com.b2b.model.Filter;
import com.b2b.model.Produit;
import com.b2b.service.FilterProduct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class FilterTestRunner implements CommandLineRunner {

    @Autowired
    private FilterProduct filterProduct;

    @Override
    public void run(String... args) throws Exception {
        // 1️⃣ Create your filter
        Filter filter = new Filter();
        filter.setNom("Laptop");
        filter.setCategorie("Informatique");
        filter.setPrixMin(new BigDecimal("500"));
        filter.setPrixMax(new BigDecimal("2000"));
        filter.setTri("desc");

        // 2️⃣ Call your filter method
        List<Produit> results = filterProduct.searchCustom(filter);

        // 3️⃣ Print the results
        System.out.println("Filtered products:");
        results.forEach(p -> System.out.println(
                p.getName() + " | " +
                        (p.getCategorie() != null ? p.getCategorie().getName() : "No Category") + " | " +
                        p.getPrice()
        ));
    }
}
