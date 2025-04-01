package com.fruitmall.domain.review.application.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateReviewRequestDto {
    
    @NotNull(message = "주문 상세 ID는 필수 입력값입니다")
    private Long orderDetailId;
    
    @NotNull(message = "평점은 필수 입력값입니다")
    @Min(value = 1, message = "평점은 최소 1점 이상이어야 합니다")
    @Max(value = 5, message = "평점은 최대 5점까지 가능합니다")
    private Integer rating;
    
    @NotBlank(message = "리뷰 내용은 필수 입력값입니다")
    private String content;
    
    private String imageUrl;
}