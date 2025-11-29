package com.b2b.service.impl;

import com.b2b.dto.ProduitCreateDTO;
import com.b2b.dto.ProduitDTO;
import com.b2b.dto.ProduitUpdateDTO;
import com.b2b.model.Produit;
import com.b2b.repository.ProduitRepository;
import com.b2b.service.ProduitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProduitServiceImpl implements ProduitService {

    private final ProduitRepository produitRepository;

    @Autowired
    public ProduitServiceImpl(ProduitRepository produitRepository) {
        this.produitRepository = produitRepository;
    }

    private ProduitDTO toDto(Produit p) {
        if (p == null) return null;
        ProduitDTO dto = new ProduitDTO();
        dto.id = p.getId();
        dto.name = p.getName();
        dto.description = p.getDescription();
        dto.price = p.getPrice();
        dto.stock = p.getStock();
        dto.sellerId = p.getSellerId();
        return dto;
    }

    private Produit fromCreateDto(ProduitCreateDTO in) {
        Produit p = new Produit();
        p.setName(in.name);
        p.setDescription(in.description);
        p.setPrice(in.price);
        p.setStock(in.stock != null ? in.stock : 0);
        p.setSellerId(in.sellerId);
        return p;
    }

    @Override
    public List<ProduitDTO> findAll(String q) {
        List<Produit> list = (q != null && !q.isBlank()) ? produitRepository.findByNameContainingIgnoreCase(q) : produitRepository.findAll();
        return list.stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    public ProduitDTO findById(Long id) {
        return produitRepository.findById(id).map(this::toDto).orElseThrow(() -> new RuntimeException("Produit non trouvé: " + id));
    }

    @Override
    public ProduitDTO create(ProduitCreateDTO in) {
        Produit saved = produitRepository.save(fromCreateDto(in));
        return toDto(saved);
    }

    @Override
    public ProduitDTO update(Long id, ProduitUpdateDTO in) {
        Produit updated = produitRepository.findById(id).map(produit -> {
            produit.setName(in.name);
            produit.setDescription(in.description);
            produit.setPrice(in.price);
            produit.setStock(in.stock != null ? in.stock : produit.getStock());
            produit.setSellerId(in.sellerId);
            return produitRepository.save(produit);
        }).orElseThrow(() -> new RuntimeException("Produit non trouvé avec l'id: " + id));
        return toDto(updated);
    }

    @Override
    public void delete(Long id) {
        produitRepository.deleteById(id);
    }

}
