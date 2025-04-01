package com.fruitmall.domain.fruit.application.dto;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Builder
public class FruitDto {
    
    private Long id;
    private String fruitName;
    private String origin;
    private Integer stockQuantity;
    private BigDecimal price;
    private Long categoryId;
    private String categoryName;
    private String season;
    private String description;
    private String imageUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}