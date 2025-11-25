package com.b2b.avis.service.impl;

import com.b2b.avis.dto.ReviewRequest;
import com.b2b.avis.model.Review;
import com.b2b.avis.repository.ReviewRepository;
import com.b2b.avis.service.ReviewService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;

    public ReviewServiceImpl(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    @Override
    public Review createReview(ReviewRequest request) {
        Review review = new Review(request.getProductId(), request.getUserId(), request.getRating(), request.getComment());
        return reviewRepository.save(review);
    }

    @Override
    public List<Review> getReviewsForProduct(Long productId) {
        return reviewRepository.findByProductIdOrderByCreatedAtDesc(productId);
    }

    @Override
    public Double getAverageRating(Long productId) {
        Double avg = reviewRepository.findAverageRatingByProductId(productId);
        return avg == null ? 0.0 : avg;
    }

    @Override
    public void deleteReview(Long id) {
        reviewRepository.deleteById(id);
    }

    @Override
    public Optional<Review> findById(Long id) {
        return reviewRepository.findById(id);
    }

    @Override
    public List<Review> getReportedReviews() {
        return reviewRepository.findByReportedTrue();
    }

    @Override
    public Review reportReview(Long id) {
        Review r = reviewRepository.findById(id).orElseThrow(() -> new RuntimeException("Review not found"));
        r.setReported(true);
        return reviewRepository.save(r);
    }
}
