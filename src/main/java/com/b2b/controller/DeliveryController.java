package com.b2b.controller;

import com.b2b.dto.DeliveryDTO;
import com.b2b.model.StatutCommande;
import com.b2b.service.DeliveryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Contrôleur REST pour gérer les livraisons
 * Endpoints accessibles via /api/deliveries/**
 */
@RestController
@RequestMapping("/deliveries")  // CHANGÉ : Retiré le /api car déjà dans context-path
@CrossOrigin(originPatterns = "*", allowCredentials = "true") // CORRIGÉ : originPatterns au lieu de origins
public class DeliveryController {

    private static final Logger logger = LoggerFactory.getLogger(DeliveryController.class);
    private final DeliveryService deliveryService;

    @Autowired
    public DeliveryController(DeliveryService deliveryService) {
        this.deliveryService = deliveryService;
    }

    /**
     * GET /api/deliveries - Liste toutes les livraisons
     */
    @GetMapping
    public ResponseEntity<List<DeliveryDTO>> getAllDeliveries() {
        logger.info("📦 GET /api/deliveries - Récupération de toutes les livraisons");
        List<DeliveryDTO> deliveries = deliveryService.getAllDeliveries();
        logger.info("✅ {} livraisons trouvées", deliveries.size());

        // Log détaillé de chaque livraison pour diagnostiquer
        deliveries.forEach(delivery -> {
            logger.debug("Livraison ID={}, trackingNumber={}, refCommande={}, transporteur={}, statut={}, frais={}",
                delivery.getId(),
                delivery.getTrackingNumber(),
                delivery.getRefCommande(),
                delivery.getTransporteur(),
                delivery.getStatut(),
                delivery.getFraisLivraison());
        });

        return ResponseEntity.ok(deliveries);
    }

    /**
     * GET /api/deliveries/{id} - Détails d'une livraison
     */
    @GetMapping("/{id}")
    public ResponseEntity<DeliveryDTO> getDeliveryById(@PathVariable Long id) {
        return deliveryService.getDeliveryById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * GET /api/deliveries/order/{orderId} - Livraisons d'une commande
     */
    @GetMapping("/order/{orderId}")
    public ResponseEntity<DeliveryDTO> getDeliveryByOrderId(@PathVariable Long orderId) {
        return deliveryService.getDeliveryByOrderId(orderId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * GET /api/deliveries/status/{status} - Filtrer par statut
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<List<DeliveryDTO>> getDeliveriesByStatus(@PathVariable String status) {
        try {
            StatutCommande statutEnum = StatutCommande.valueOf(status.toUpperCase());
            List<DeliveryDTO> deliveries = deliveryService.getDeliveriesByStatus(statutEnum);
            return ResponseEntity.ok(deliveries);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * POST /api/deliveries - Créer une livraison
     */
    @PostMapping
    public ResponseEntity<?> createDelivery(@RequestBody Map<String, Object> deliveryData) {
        try {
            // Accepter orderId ou commandeId
            Long commandeId;
            if (deliveryData.containsKey("orderId")) {
                commandeId = Long.valueOf(deliveryData.get("orderId").toString());
            } else if (deliveryData.containsKey("commandeId")) {
                commandeId = Long.valueOf(deliveryData.get("commandeId").toString());
            } else {
                Map<String, String> error = new HashMap<>();
                error.put("error", "orderId ou commandeId requis");
                return ResponseEntity.badRequest().body(error);
            }

            // Récupérer le transporteur (carrier ou transporteur)
            String carrier = null;
            if (deliveryData.containsKey("carrier")) {
                carrier = deliveryData.get("carrier").toString();
            } else if (deliveryData.containsKey("transporteur")) {
                carrier = deliveryData.get("transporteur").toString();
            }

            // Récupérer les données d'adresse si présentes
            Map<String, Object> shippingAddress = null;
            if (deliveryData.containsKey("shippingAddress")) {
                shippingAddress = (Map<String, Object>) deliveryData.get("shippingAddress");
            }

            // Récupérer shippingCost si présent
            Double shippingCost = null;
            if (deliveryData.containsKey("shippingCost")) {
                shippingCost = Double.valueOf(deliveryData.get("shippingCost").toString());
            }

            DeliveryDTO delivery = deliveryService.createDeliveryForOrder(
                commandeId, carrier, shippingAddress, shippingCost);
            return ResponseEntity.status(HttpStatus.CREATED).body(delivery);
        } catch (IllegalArgumentException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Erreur lors de la création de la livraison: " + e.getMessage());
            e.printStackTrace(); // Pour debug
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    /**
     * POST /api/deliveries/{id}/status - Changer le statut
     */
    @PostMapping("/{id}/status")
    public ResponseEntity<DeliveryDTO> updateDeliveryStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> statusData) {
        try {
            String newStatus = statusData.get("status");
            StatutCommande statutEnum = StatutCommande.valueOf(newStatus.toUpperCase());

            DeliveryDTO delivery = deliveryService.updateDeliveryStatus(id, statutEnum);
            return ResponseEntity.ok(delivery);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * GET /api/deliveries/calculate-shipping?city={city} - Calculer les frais
     */
    @GetMapping("/calculate-shipping")
    public ResponseEntity<Map<String, Object>> calculateShipping(@RequestParam String city) {
        double frais = deliveryService.calculateShippingCost(city);

        Map<String, Object> response = new HashMap<>();
        response.put("city", city);
        response.put("shippingCost", frais);
        response.put("currency", "DH");

        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/deliveries/track/{trackingNumber} - Suivre une livraison
     */
    @GetMapping("/track/{trackingNumber}")
    public ResponseEntity<DeliveryDTO> trackDelivery(@PathVariable String trackingNumber) {
        return deliveryService.trackDelivery(trackingNumber)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * DELETE /api/deliveries/{id} - Supprimer une livraison
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDelivery(@PathVariable Long id) {
        try {
            deliveryService.deleteDelivery(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
