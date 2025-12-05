package com.b2b.repository;

import com.b2b.model.Produit;
import com.b2b.model.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByProductOrderByCreatedAtDesc(Produit product);

    List<Review> findTop3ByProductOrderByCreatedAtDesc(Produit product);

    long countByProduct(Produit product);

    Page<Review> findByProduct(Produit product, Pageable pageable);

}
