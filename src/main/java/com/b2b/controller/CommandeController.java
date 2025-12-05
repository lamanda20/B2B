package com.b2b.controller;

import com.b2b.dto.CommandeDto;
import com.b2b.mapper.CommandeMapper;
import com.b2b.model.StatutCommande;
import com.b2b.service.CommandeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/commandes")
@CrossOrigin(originPatterns = "*", allowCredentials = "true")
public class CommandeController {

    private final CommandeService commandeService;

    @Autowired
    public CommandeController(CommandeService commandeService) {
        this.commandeService = commandeService;
    }

    // ==========================
    // GET ALL COMMANDES
    // ==========================
    @GetMapping
    public ResponseEntity<List<CommandeDto>> getAllCommandes() {
        return ResponseEntity.ok(
                commandeService.findAll()
                        .stream()
                        .map(CommandeMapper::toDto)
                        .collect(Collectors.toList())
        );
    }

    // ==========================
    // GET BY ID
    // ==========================
    @GetMapping("/{id}")
    public ResponseEntity<CommandeDto> getCommandeById(@PathVariable Long id) {
        return commandeService.findById(id)
                .map(c -> ResponseEntity.ok(CommandeMapper.toDto(c)))
                .orElse(ResponseEntity.notFound().build());
    }

    // ==========================
    // GET BY REFERENCE
    // ==========================
    @GetMapping("/ref/{refCommande}")
    public ResponseEntity<CommandeDto> getCommandeByRef(@PathVariable String refCommande) {
        return commandeService.findByRefCommande(refCommande)
                .map(c -> ResponseEntity.ok(CommandeMapper.toDto(c)))
                .orElse(ResponseEntity.notFound().build());
    }

    // ==========================
    // GET ORDERS BY BUYER COMPANY
    // ==========================
    @GetMapping("/Company/{CompanyId}")
    public ResponseEntity<List<CommandeDto>> getCommandesByCompany(@PathVariable Long CompanyId) {
        return ResponseEntity.ok(
                commandeService.findByCompany(CompanyId)
                        .stream()
                        .map(CommandeMapper::toDto)
                        .collect(Collectors.toList())
        );
    }

    // ==========================
    // CREATE COMMAND
    // ==========================
    @PostMapping
    public ResponseEntity<CommandeDto> createCommande(@RequestBody com.b2b.model.Commande commande) {
        var created = commandeService.create(commande);
        return ResponseEntity.ok(CommandeMapper.toDto(created));
    }

    // ==========================
    // VALIDATE COMMAND
    // ==========================
    @PostMapping("/{id}/valider")
    public ResponseEntity<Map<String, Object>> validerCommande(@PathVariable Long id) {
        try {
            StatutCommande statut = commandeService.validerCommande(id);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Commande validée avec succès");
            response.put("statut", statut);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // ==========================
    // UPDATE STATUS
    // ==========================
    @PutMapping("/{id}/statut")
    public ResponseEntity<CommandeDto> updateStatut(
            @PathVariable Long id,
            @RequestBody Map<String, String> data) {

        try {
            StatutCommande statut = StatutCommande.valueOf(data.get("statut").toUpperCase());
            var updated = commandeService.updateStatut(id, statut);
            return ResponseEntity.ok(CommandeMapper.toDto(updated));

        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // ==========================
    // GET TOTAL
    // ==========================
    @GetMapping("/{id}/total")
    public ResponseEntity<Map<String, Double>> calculerTotal(@PathVariable Long id) {
        try {
            double total = commandeService.calculerTotal(id);
            return ResponseEntity.ok(Map.of("total", total));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // ==========================
    // DELETE COMMAND
    // ==========================
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCommande(@PathVariable Long id) {
        commandeService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // ==========================
    // GET SELLER ORDERS
    // ==========================
    @GetMapping("/seller/{sellerId}")
    public ResponseEntity<List<CommandeDto>> getOrdersForSeller(@PathVariable Long sellerId) {
        return ResponseEntity.ok(
                commandeService.findOrdersForSeller(sellerId)
                        .stream()
                        .map(CommandeMapper::toDto)
                        .collect(Collectors.toList())
        );
    }

    // ==========================
    // SELLER DECISION
    // ==========================
    @PutMapping("/{id}/sellerDecision")
    public ResponseEntity<CommandeDto> sellerDecision(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {

        String status = body.get("statut");  // "VALIDEE", "REFUSEE"
        StatutCommande st = StatutCommande.valueOf(status);

        var updated = commandeService.updateStatus(id, st);

        return ResponseEntity.ok(CommandeMapper.toDto(updated));
    }
}
