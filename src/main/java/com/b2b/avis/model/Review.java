package com.b2b.avis.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

@Entity
@Table(name = "reviews")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long productId; // ID du produit concerné

    private Long userId; // ID de l’acheteur qui laisse l’avis

    private int rating; // note entre 1 et 5

    @Column(length = 500)
    private String comment; // commentaire

    private boolean reported = false; // avis signalé

    private String createdAt;

    // Constructeur utilitaire utilisé par le service (productId, userId, rating, comment)
    public Review(Long productId, Long userId, Integer rating, String comment) {
        this.productId = productId;
        this.userId = userId;
        this.rating = rating != null ? rating : 0;
        this.comment = comment;
        this.createdAt = Instant.now().toString();
    }

}
