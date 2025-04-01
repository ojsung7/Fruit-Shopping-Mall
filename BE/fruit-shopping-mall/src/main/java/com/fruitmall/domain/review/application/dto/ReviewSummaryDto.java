package com.fruitmall.domain.review.application.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ReviewSummaryDto {
    
    private Long fruitId;
    private String fruitName;
    private Double averageRating;
    private Long reviewCount;
    private List<ReviewDto> reviews;
}