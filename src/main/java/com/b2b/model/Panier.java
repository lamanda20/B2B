package com.b2b.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "paniers")
@Data
public class Panier {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "app_user_id")
    private AppUser appUser;

    @OneToMany(mappedBy = "panier", cascade = CascadeType.ALL)
    private List<LignePanier> lignePaniers;

    private LocalDate dateCreation;

    // Méthode pour calculer le total
    public double getTotal() {
        if (lignePaniers == null || lignePaniers.isEmpty()) {
            return 0.0;
        }
        return lignePaniers.stream()
                .mapToDouble(LignePanier::getSousTotal)
                .sum();
    }
}
