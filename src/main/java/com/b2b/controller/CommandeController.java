package com.b2b.controller;

import com.b2b.model.Commande;
import com.b2b.repository.CommandeRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/commandes")
public class CommandeController {

    private final CommandeRepository commandeRepository;

    public CommandeController(CommandeRepository commandeRepository) {
        this.commandeRepository = commandeRepository;
    }

    // Get all commandes
    @GetMapping
    public List<Commande> getAll() {
        return commandeRepository.findAll();
    }

    // Get by id
    @GetMapping("/{id}")
    public Commande getById(@PathVariable Long id) {
        return commandeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Commande not found"));
    }

    // Create new commande
    @PostMapping
    public Commande create(@RequestBody Commande commande) {
        return commandeRepository.save(commande);
    }

    // Delete by id
    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        commandeRepository.deleteById(id);
        return "Commande deleted";
    }
}
