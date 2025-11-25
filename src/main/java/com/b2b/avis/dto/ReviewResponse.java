package com.b2b.avis.dto;

import lombok.Data;

@Data
public class ReviewResponse {

    private Long id;
    private Long productId;
    private Long userId;
    private int rating;
    private String comment;
    private String createdAt;
}
