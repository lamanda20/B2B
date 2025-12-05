package com.b2b.service;

import com.b2b.dto.ReviewCreateRequest;
import com.b2b.dto.ReviewDto;

import java.util.List;

public interface ReviewService {

    ReviewDto addReview(ReviewCreateRequest req);

    List<ReviewDto> getAllForProduct(Long productId);

    List<ReviewDto> getLatestForProduct(Long productId, int limit);
}
