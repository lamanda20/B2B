package com.b2b.controller;

import com.b2b.dto.*;
import com.b2b.service.ProduitService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/products")
public class ProduitController {

    private final ProduitService service;
    public ProduitController(ProduitService service) { this.service = service; }


    @GetMapping
    public List<ProduitDTO> list(@RequestParam(required = false) String q) {
        return service.findAll(q);
    }


    @GetMapping("/{id}")
    public ProduitDTO get(@PathVariable Long id) { return service.findById(id); }


    @PostMapping
    public ResponseEntity<ProduitDTO> create(@Valid @RequestBody ProduitCreateDTO in) {
        return ResponseEntity.ok(service.create(in));
    }


    @PutMapping("/{id}")
    public ProduitDTO update(@PathVariable Long id, @Valid @RequestBody ProduitUpdateDTO in) {
        return service.update(id, in);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
