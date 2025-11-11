package com.b2b.core;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "companies")
@Data
@JsonIgnoreProperties({"users", "produits", "hibernateLazyInitializer", "handler"})
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    private String name;
    private String address;
    private String city;
    private String phone;
    private String website;

    @OneToMany(mappedBy = "company")
    @JsonIgnoreProperties({"company"})
    private List<AppUser> users;

    @OneToMany(mappedBy = "company")
    @JsonIgnoreProperties({"company"})
    private List<Produit> produits;

    // Méthode pour obtenir les clients (utilisateurs avec rôle BUYER)
    public List<AppUser> getClients() {
        return users.stream()
                .filter(user -> user.getRole() == Role.BUYER)
                .toList();
    }

    // Méthode pour obtenir les produits
    public List<Produit> getProducts() {
        return produits;
    }
}

