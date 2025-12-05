package com.b2b.controller;

import com.b2b.dto.ReviewCreateRequest;
import com.b2b.dto.ReviewDto;
import com.b2b.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
@CrossOrigin(originPatterns = "*", allowCredentials = "true")
public class ReviewController {

    private final ReviewService reviewService;

    // ---------- Add a new review ----------
    @PostMapping
    public ResponseEntity<ReviewDto> add(@RequestBody ReviewCreateRequest req) {
        ReviewDto dto = reviewService.addReview(req);
        return ResponseEntity.ok(dto);
    }

    // ---------- Get all reviews for a product ----------
    @GetMapping("/product/{productId}")
    public ResponseEntity<List<ReviewDto>> getAll(@PathVariable Long productId) {
        return ResponseEntity.ok(reviewService.getAllForProduct(productId));
    }

    // ---------- Get latest N reviews (default 3) ----------
    @GetMapping("/product/{productId}/latest")
    public ResponseEntity<List<ReviewDto>> getLatest(
            @PathVariable Long productId,
            @RequestParam(defaultValue = "3") int limit
    ) {
        return ResponseEntity.ok(reviewService.getLatestForProduct(productId, limit));
    }
}
