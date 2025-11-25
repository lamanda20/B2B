package com.b2b.avis.controller;

import com.b2b.avis.dto.ReviewRequest;
import com.b2b.avis.dto.ReviewResponse;
import com.b2b.avis.model.Review;
import com.b2b.avis.service.ReviewService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping
    public ResponseEntity<ReviewResponse> postReview(@Valid @RequestBody ReviewRequest request) {
        Review saved = reviewService.createReview(request);
        ReviewResponse resp = toResponse(saved);
        return ResponseEntity.ok(resp);
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<ReviewResponse>> getByProduct(@PathVariable Long productId) {
        List<ReviewResponse> list = reviewService.getReviewsForProduct(productId).stream()
                .map(this::toResponse).collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    @GetMapping("/product/{productId}/rating")
    public ResponseEntity<Double> getAverage(@PathVariable Long productId) {
        return ResponseEntity.ok(reviewService.getAverageRating(productId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        reviewService.deleteReview(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/report")
    public ResponseEntity<Void> report(@PathVariable Long id) {
        reviewService.reportReview(id);
        return ResponseEntity.ok().build();
    }

    private ReviewResponse toResponse(Review r) {
        ReviewResponse res = new ReviewResponse();
        res.setId(r.getId());
        res.setProductId(r.getProductId());
        res.setUserId(r.getUserId());
        res.setRating(r.getRating());
        res.setComment(r.getComment());
        res.setCreatedAt(r.getCreatedAt());
        return res;
    }
}
