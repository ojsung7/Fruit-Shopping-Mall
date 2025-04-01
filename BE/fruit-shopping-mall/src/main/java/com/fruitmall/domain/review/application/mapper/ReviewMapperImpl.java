package com.fruitmall.domain.review.application.mapper;

import com.fruitmall.domain.review.application.dto.ReviewDto;
import com.fruitmall.domain.review.application.dto.ReviewSummaryDto;
import com.fruitmall.domain.review.domain.Review;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ReviewMapperImpl implements ReviewMapper {

    @Override
    public ReviewDto toDto(Review review) {
        if (review == null) {
            return null;
        }
        
        return ReviewDto.builder()
                .id(review.getId())
                .orderDetailId(review.getOrderDetail() != null ? review.getOrderDetail().getId() : null)
                .memberId(review.getMember() != null ? review.getMember().getId() : null)
                .memberName(review.getMember() != null ? review.getMember().getName() : null)
                .fruitId(review.getOrderDetail() != null && review.getOrderDetail().getFruit() != null ? 
                         review.getOrderDetail().getFruit().getId() : null)
                .fruitName(review.getOrderDetail() != null && review.getOrderDetail().getFruit() != null ? 
                           review.getOrderDetail().getFruit().getFruitName() : null)
                .fruitImageUrl(review.getOrderDetail() != null && review.getOrderDetail().getFruit() != null ? 
                               review.getOrderDetail().getFruit().getImageUrl() : null)
                .rating(review.getRating())
                .content(review.getContent())
                .reviewDate(review.getReviewDate())
                .imageUrl(review.getImageUrl())
                .createdAt(review.getCreatedAt())
                .updatedAt(review.getUpdatedAt())
                .build();
    }

    @Override
    public List<ReviewDto> toDtoList(List<Review> reviews) {
        if (reviews == null) {
            return null;
        }
        
        return reviews.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public ReviewSummaryDto toSummaryDto(Long fruitId, String fruitName, Double averageRating, Long reviewCount, List<ReviewDto> reviews) {
        return ReviewSummaryDto.builder()
                .fruitId(fruitId)
                .fruitName(fruitName)
                .averageRating(averageRating != null ? averageRating : 0.0)
                .reviewCount(reviewCount != null ? reviewCount : 0L)
                .reviews(reviews)
                .build();
    }
}