package com.b2b.service.impl;

import com.b2b.dto.ReviewCreateRequest;
import com.b2b.dto.ReviewDto;
import com.b2b.model.Company;
import com.b2b.model.Produit;
import com.b2b.model.Review;
import com.b2b.repository.CompanyRepository;
import com.b2b.repository.ProduitRepository;
import com.b2b.repository.ReviewRepository;
import com.b2b.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepo;
    private final ProduitRepository produitRepo;
    private final CompanyRepository companyRepo;

    @Override
    public ReviewDto addReview(ReviewCreateRequest req) {

        Produit product = produitRepo.findById(req.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("Produit introuvable"));

        Company company = null;
        String authorName = "Client";

        if (req.getCompanyId() != null) {
            company = companyRepo.findById(req.getCompanyId())
                    .orElse(null);
            if (company != null) {
                authorName = company.getName() != null
                        ? company.getName()
                        : company.getEmail();
            }
        }

        Review review = new Review();
        review.setProduct(product);
        review.setCompany(company);
        review.setAuthorName(authorName);
        review.setRating(req.getRating());
        review.setComment(req.getComment());

        Review saved = reviewRepo.save(review);
        return toDto(saved);
    }

    @Override
    public List<ReviewDto> getAllForProduct(Long productId) {
        Produit product = produitRepo.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Produit introuvable"));

        return reviewRepo.findByProductOrderByCreatedAtDesc(product)
                .stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public List<ReviewDto> getLatestForProduct(Long productId, int limit) {

        Produit product = produitRepo.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Produit introuvable"));

        // Use Spring Data paging instead of Specification
        var page = reviewRepo.findByProduct(
                product,
                PageRequest.of(0, limit)
        );

        return page.getContent()
                .stream()
                .map(this::toDto)
                .toList();
    }


    private ReviewDto toDto(Review r) {
        return new ReviewDto(
                r.getId(),
                r.getProduct().getId(),
                r.getCompany() != null ? r.getCompany().getId() : null,
                r.getAuthorName(),
                r.getRating(),
                r.getComment(),
                r.getCreatedAt()
        );
    }
}
