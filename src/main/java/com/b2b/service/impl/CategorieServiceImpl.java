package com.b2b.service.impl;

import com.b2b.model.Categorie;
import com.b2b.repository.CategorieRepository;
import com.b2b.service.CategorieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CategorieServiceImpl implements CategorieService {

    private final CategorieRepository categorieRepository;

    @Autowired
    public CategorieServiceImpl(CategorieRepository categorieRepository) {
        this.categorieRepository = categorieRepository;
    }

    @Override
    public List<Categorie> findAll() {
        return categorieRepository.findAll();
    }

    @Override
    public Optional<Categorie> findById(Integer id) {
        return categorieRepository.findById(id);
    }

    @Override
    public Optional<Categorie> findByName(String name) {
        return categorieRepository.findByName(name);
    }

    @Override
    public Categorie create(Categorie categorie) {
        if (categorieRepository.existsByName(categorie.getName())) {
            throw new RuntimeException("Une catégorie avec ce nom existe déjà");
        }
        return categorieRepository.save(categorie);
    }

    @Override
    public Categorie update(Integer id, Categorie categorieDetails) {
        return categorieRepository.findById(id)
                .map(categorie -> {
                    categorie.setName(categorieDetails.getName());
                    categorie.setDescription(categorieDetails.getDescription());
                    return categorieRepository.save(categorie);
                })
                .orElseThrow(() -> new RuntimeException("Catégorie non trouvée avec l'id: " + id));
    }

    @Override
    public void delete(Integer id) {
        categorieRepository.deleteById(id);
    }
}
