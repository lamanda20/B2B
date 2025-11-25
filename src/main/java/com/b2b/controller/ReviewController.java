package com.b2b.controller;

import com.b2b.avis.dto.ReviewRequest;
import com.b2b.avis.model.Review;
import com.b2b.avis.service.ReviewService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping
    public Review addReview(@RequestBody ReviewRequest request) {
        return reviewService.createReview(request);
    }

    @GetMapping("/product/{productId}")
    public List<Review> getReviews(@PathVariable Long productId) {
        return reviewService.getReviewsForProduct(productId);
    }

    @GetMapping("/product/{productId}/average")
    public double getAverage(@PathVariable Long productId) {
        Double avg = reviewService.getAverageRating(productId);
        return avg != null ? avg : 0.0;
    }

    @PutMapping("/{id}/report")
    public void report(@PathVariable Long id) {
        reviewService.reportReview(id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        reviewService.deleteReview(id);
    }
}
