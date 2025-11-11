package com.b2b.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "app_users")
@Data
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "created_at")
    private LocalDate createdAt;

    private String email;
    private Boolean enabled;

    @Column(name = "full_name")
    private String fullName;

    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    @ManyToOne
    @JoinColumn(name = "company_id")
    @JsonIgnoreProperties({"users", "produits"})
    private Company company;

    @Column(name = "must_change_password")
    private Boolean mustChangePassword;

    @OneToOne(mappedBy = "appUser", cascade = CascadeType.ALL)
    @JsonIgnoreProperties({"appUser"})
    private Panier panier;

    @OneToMany(mappedBy = "appUser")
    @JsonIgnoreProperties({"appUser"})
    private List<Payment> payments;

    @OneToMany(mappedBy = "client")
    @JsonIgnoreProperties({"client", "lignes", "livraison", "payments"})
    private List<Commande> commandes;

}
