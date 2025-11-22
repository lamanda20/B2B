package com.b2b.service;

import com.b2b.model.Categorie;

import java.util.List;
import java.util.Optional;

public interface CategorieService {
    List<Categorie> findAll();
    Optional<Categorie> findById(Integer id);
    Optional<Categorie> findByName(String name);
    Categorie create(Categorie categorie);
    Categorie update(Integer id, Categorie categorieDetails);
    void delete(Integer id);
}
