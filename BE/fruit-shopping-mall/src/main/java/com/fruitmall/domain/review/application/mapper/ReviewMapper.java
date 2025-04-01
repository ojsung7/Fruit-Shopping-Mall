package com.fruitmall.domain.review.application.mapper;

import com.fruitmall.domain.review.application.dto.ReviewDto;
import com.fruitmall.domain.review.application.dto.ReviewSummaryDto;
import com.fruitmall.domain.review.domain.Review;

import java.util.List;

public interface ReviewMapper {
    
    ReviewDto toDto(Review review);
    
    List<ReviewDto> toDtoList(List<Review> reviews);
    
    ReviewSummaryDto toSummaryDto(Long fruitId, String fruitName, Double averageRating, Long reviewCount, List<ReviewDto> reviews);
}