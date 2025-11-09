package com.b2b.repository;

import com.b2b.model.Delivery;
import com.b2b.model.DeliveryStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository pour l'entité Delivery
 */
@Repository
public interface DeliveryRepository extends JpaRepository<Delivery, Long> {

    // Recherche par statut
    List<Delivery> findByStatus(DeliveryStatus status);

    // Recherche par commande
    Optional<Delivery> findByOrderId(Long orderId);

    // Recherche par numéro de tracking
    Optional<Delivery> findByTrackingNumber(String trackingNumber);

    // Recherche par transporteur
    List<Delivery> findByCarrier(String carrier);

    // Recherche par ville (via ShippingAddress embedded)
    @Query("SELECT d FROM Delivery d WHERE d.shippingAddress.city = :city")
    List<Delivery> findByCity(@Param("city") String city);

    // Recherche par utilisateur
    List<Delivery> findByUserId(Long userId);

    // Livraisons en cours (statuts actifs)
    @Query("SELECT d FROM Delivery d WHERE d.status IN ('IN_TRANSIT', 'SHIPPED', 'IN_PREPARATION')")
    List<Delivery> findActiveDeliveries();
}

