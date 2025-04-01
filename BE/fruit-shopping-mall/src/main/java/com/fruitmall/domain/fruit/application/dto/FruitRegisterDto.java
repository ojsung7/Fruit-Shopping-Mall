package com.fruitmall.domain.fruit.application.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FruitRegisterDto {
    
    @NotBlank(message = "상품명은 필수 입력값입니다")
    private String fruitName;
    
    private String origin;
    
    @NotNull(message = "재고 수량은 필수 입력값입니다")
    @Min(value = 0, message = "재고 수량은 0 이상이어야 합니다")
    private Integer stockQuantity;
    
    @NotNull(message = "가격은 필수 입력값입니다")
    @Positive(message = "가격은 양수여야 합니다")
    private BigDecimal price;
    
    @NotNull(message = "카테고리 ID는 필수 입력값입니다")
    private Long categoryId;
    
    private String season;
    
    private String description;
    
    private String imageUrl;
}