package com.b2b.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "categories")
@Data
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@AllArgsConstructor
@NoArgsConstructor
public class Categorie {

    @Id
    @Column(name = "id_cat")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idCat;

    @Column(nullable = true)
    private String description;

    @Column(nullable = false)
    private String name;

}
