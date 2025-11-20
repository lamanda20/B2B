package com.b2b.repository;

import com.b2b.model.Avis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AvisRepository extends JpaRepository<Avis, Long> {
    List<Avis> findByProduitId(Long produitId);
    List<Avis> findByCompanyId(Long CompanyId);
    List<Avis> findByNote(int note);

    @Query("SELECT AVG(a.note) FROM Avis a WHERE a.produit.id = :produitId")
    Double findAverageEvaluationByProduitId(Long produitId);
}
