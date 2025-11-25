package com.b2b.avis.service;

import com.b2b.avis.dto.ReviewRequest;
import com.b2b.avis.model.Review;

import java.util.List;
import java.util.Optional;

public interface ReviewService {
    Review createReview(ReviewRequest request);
    List<Review> getReviewsForProduct(Long productId);
    Double getAverageRating(Long productId);
    void deleteReview(Long id);
    Optional<Review> findById(Long id);
    List<Review> getReportedReviews();
    Review reportReview(Long id); // toggle reported flag
}
