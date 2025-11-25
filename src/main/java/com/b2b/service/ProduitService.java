package com.b2b.service;

import com.b2b.dto.ProduitDTO;
import com.b2b.model.Filter;
import com.b2b.model.Produit;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProduitService {

    private final FilterProduct filterProduct;

    public ProduitService(FilterProduct filterProduct) {
        this.filterProduct = filterProduct;
    }

    // Delegate filtering to your independent FilterProduct
    public List<Produit> search(Filter f) {
        return filterProduct.searchCustom(f);
    }

