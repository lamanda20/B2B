package com.b2b.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDto {

    private Long id;
    private Long productId;
    private Long companyId;   // can be null
    private String authorName;
    private int rating;
    private String comment;
    private LocalDateTime createdAt;
}
