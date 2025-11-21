package com.b2b.avis.repository;

import com.b2b.avis.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByProductIdOrderByCreatedAtDesc(Long productId);

    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.productId = :productId")
    Double findAverageRatingByProductId(Long productId);

    List<Review> findByReportedTrue();
}
