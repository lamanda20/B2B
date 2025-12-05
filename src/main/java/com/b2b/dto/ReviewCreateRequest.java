package com.b2b.dto;

import lombok.Data;

@Data
public class ReviewCreateRequest {
    private Long productId;
    private Long companyId;   // from JWT/AuthStore
    private int rating;       // 1..5
    private String comment;
}
