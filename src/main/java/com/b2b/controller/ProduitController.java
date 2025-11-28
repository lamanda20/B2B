package com.b2b.controller;

import com.b2b.dto.FilterRequest;
import com.b2b.dto.ProductDto;
import com.b2b.mapper.DtoMapper;
import com.b2b.model.Produit;
import com.b2b.service.ProduitService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(originPatterns = "*", allowCredentials = "true")
@RequiredArgsConstructor
public class ProduitController {

    private final ProduitService produitService;

    @GetMapping
    public ResponseEntity<List<ProductDto>> findAll() {
        var list = produitService.findAll()
                .stream()
                .map(DtoMapper::toDto)
                .toList();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> findById(@PathVariable Long id) {
        return produitService.findById(id)
                .map(DtoMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ProductDto> create(@RequestBody ProductDto dto) {
        var created = produitService.create(dto);
        return ResponseEntity.ok(DtoMapper.toDto(created));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> update(@PathVariable Long id, @RequestBody ProductDto dto) {
        try {
            var updated = produitService.update(id, dto);
            return ResponseEntity.ok(DtoMapper.toDto(updated));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        produitService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/company/{companyId}")
    public ResponseEntity<List<ProductDto>> findByCompany(@PathVariable Long companyId) {
        var dtos = produitService.findByCompany(companyId)
                .stream()
                .map(DtoMapper::toDto)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/categories/{id}")
    public ResponseEntity<List<ProductDto>> findByCategorie(@PathVariable("id") Integer id) {

        var products = produitService.findByCategorie(id)
                .stream()
                .map(DtoMapper::toDto)
                .toList();

        return ResponseEntity.ok(products);
    }


    @GetMapping("/search")
    public ResponseEntity<List<ProductDto>> searchByName(@RequestParam String name) {
        var dtos = produitService.searchByName(name)
                .stream()
                .map(DtoMapper::toDto)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/in-stock")
    public ResponseEntity<List<ProductDto>> findInStock() {
        var dtos = produitService.findInStock()
                .stream()
                .map(DtoMapper::toDto)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/trending")
    public ResponseEntity<List<ProductDto>> trending() {
        var dtos = produitService.findInStock()
                .stream()
                .limit(10)
                .map(DtoMapper::toDto)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    @PostMapping("/filter")
    public ResponseEntity<List<Produit>> filterProducts(@RequestBody FilterRequest req) {
        List<Produit> result = produitService.filter(req.getCategoryId(), req.getFilters());
        return ResponseEntity.ok(result);
    }

}
