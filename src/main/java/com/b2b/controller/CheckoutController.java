package com.b2b.controller;

import com.b2b.model.Commande;
import com.b2b.service.CommandeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/checkout")
@CrossOrigin(originPatterns = "*", allowCredentials = "true")
@RequiredArgsConstructor
public class CheckoutController {

    private final CommandeService commandeService;

    @PostMapping("/place/{companyId}")
    public ResponseEntity<?> placeOrder(
            @PathVariable Long companyId,
            @RequestBody Commande commande
    ) {
        try {
            // set company ID on the incoming order
            if (commande.getCompany() == null)
                commande.setCompany(new com.b2b.model.Company());

            commande.getCompany().setId(companyId);

            // save command
            Commande saved = commandeService.create(commande);

            return ResponseEntity.ok(saved);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
