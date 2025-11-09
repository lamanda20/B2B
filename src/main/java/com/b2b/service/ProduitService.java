package com.b2b.service;

import com.b2b.dto.*;
import com.b2b.exception.NotFoundException;
import com.b2b.model.Produit;
import com.b2b.repository.ProduitRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProduitService {
    private final ProduitRepository repo;

    public ProduitService(ProduitRepository repo) {
        this.repo = repo;
    }

    public List<ProduitDTO> findAll(String q) {
        List<Produit> list = (q == null || q.isBlank())
                ? repo.findAll()
                : repo.findByNameContainingIgnoreCase(q);
        return list.stream().map(this::toDTO).collect(Collectors.toList());
    }

    public ProduitDTO findById(Long id) {
        return toDTO(repo.findById(id).orElseThrow(() ->
                new NotFoundException("Produit " + id + " introuvable")));
    }

    public ProduitDTO create(ProduitCreateDTO in) {
        Produit p = new Produit();
        p.setName(in.name);
        p.setDescription(in.description);
        p.setPrice(in.price);
        p.setStock(in.stock);
        p.setSellerId(in.sellerId);
        return toDTO(repo.save(p));
    }

    public ProduitDTO update(Long id, ProduitUpdateDTO in) {
        Produit p = repo.findById(id).orElseThrow(() ->
                new NotFoundException("Produit " + id + " introuvable"));
        p.setName(in.name);
        p.setDescription(in.description);
        p.setPrice(in.price);
        p.setStock(in.stock);
        p.setSellerId(in.sellerId);
        return toDTO(repo.save(p));
    }

    public void delete(Long id) {
        if (!repo.existsById(id))
            throw new NotFoundException("Produit " + id + " introuvable");
        repo.deleteById(id);
    }

    private ProduitDTO toDTO(Produit p) {
        ProduitDTO d = new ProduitDTO();
        d.id = p.getId();
        d.name = p.getName();
        d.description = p.getDescription();
        d.price = p.getPrice();
        d.stock = p.getStock();
        d.sellerId = p.getSellerId();
        return d;
    }
}
