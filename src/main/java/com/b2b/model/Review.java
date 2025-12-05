package com.b2b.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ---- Product ----
    @ManyToOne(optional = false)
    private Produit product;

    // ---- Who wrote it (optional link to Company) ----
    @ManyToOne(optional = true)
    private Company company;

    @Column(nullable = false, length = 180)
    private String authorName;   // e.g. company name or email

    @Column(nullable = false)
    private int rating;          // 1..5

    @Column(nullable = false, length = 1000)
    private String comment;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @PrePersist
    void prePersist() {
        if (createdAt == null) createdAt = LocalDateTime.now();
    }
}
