package com.b2b.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Entity
@Table(name = "clients")
@Data
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;
    private String email;

    // --- VOS AJOUTS ICI ---
    private String telephone;
    private String adresseParDefaut;
    private String villeParDefaut;

    // --- Relations existantes (Exemple) ---
    // NOTE: la relation `commandes` vers `Commande` a été retirée car `Commande` utilise désormais `User`.
    // Si vous souhaitez garder une association entre Client et Commande, réintroduisez un champ
    // compatible dans `Commande` (ex: private Client client) et remettez `mappedBy = "client"`.

    @OneToOne(mappedBy = "client")
    private Panier panier;

    // ... autres méthodes et attributs ...
}