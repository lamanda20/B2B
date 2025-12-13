package com.b2b.service;

import com.b2b.dto.CategoryDto;
import com.b2b.model.Categorie;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface CategorieService {



    List<Categorie> findAll();

    Optional<Categorie> findById(Integer id);

    Optional<Categorie> findByName(String name);

    Categorie create(CategoryDto dto);

    Categorie update(Integer id, CategoryDto dto);

    void delete(Integer id);

    Map<String, List<String>> getFiltersForCategory(Integer id);
}
