package com.b2b.repository;

import com.b2b.model.Categorie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategorieRepository extends JpaRepository<Categorie, Long> {

    Optional<Categorie> findByName(String name);

    List<Categorie> findByNameContainingIgnoreCase(String name);

    boolean existsByName(String name);
}