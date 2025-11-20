package com.b2b.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "companies")
@Data
@JsonIgnoreProperties({"produits", "products", "hibernateLazyInitializer", "handler"})
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
    private String email;
    private String password;
    private boolean mustChangePassword;
    private String fullName;
    // Setter for role
    private Role role;
    private boolean enabled;

    @OneToMany(mappedBy = "company")
    @JsonIgnoreProperties({"company"})
    private List<Produit> produits;

    // Méthode pour obtenir les produits
    @JsonIgnore
    public List<Produit> getProducts() {
        return produits;
    }

}