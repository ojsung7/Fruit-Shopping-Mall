package com.fruitmall.domain.review.application.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
public class ReviewDto {
    
    private Long id;
    private Long orderDetailId;
    private Long memberId;
    private String memberName;
    private Long fruitId;
    private String fruitName;
    private String fruitImageUrl;
    private Integer rating;
    private String content;
    private LocalDate reviewDate;
    private String imageUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}