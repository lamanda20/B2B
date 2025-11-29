package com.b2b.service;

import com.b2b.dto.ProduitCreateDTO;
import com.b2b.dto.ProduitDTO;
import com.b2b.dto.ProduitUpdateDTO;

import java.util.List;

public interface ProduitService {
    List<ProduitDTO> findAll(String q);
    ProduitDTO findById(Long id);
    ProduitDTO create(ProduitCreateDTO in);
    ProduitDTO update(Long id, ProduitUpdateDTO in);
    void delete(Long id);
}
