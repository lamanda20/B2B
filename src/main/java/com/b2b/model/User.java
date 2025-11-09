package com.b2b.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "users")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;
    private String email;
    private String telephone;
    private String adresse;
    private String ville;

    // Relation avec les livraisons
    @OneToMany(mappedBy = "user")
    private java.util.List<Livraison> livraisons;
}

