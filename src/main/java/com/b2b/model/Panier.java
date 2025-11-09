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
    @JoinColumn(name = "client_id")
    private Client client;

    @OneToMany(mappedBy = "panier", cascade = CascadeType.ALL)
    private List<LignePanier> lignePaniers;

    private LocalDate dateCreation;

    //-total : double
    public double getTotal() {
        return lignePaniers.stream()
                .mapToDouble(LignePanier::getSousTotal)
                .sum();
    }
}
