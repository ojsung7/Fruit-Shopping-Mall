package com.fruitmall.domain.cart.application.dto;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
public class CartDto {
    
    private Long id;
    private Long memberId;
    private Long fruitId;
    private String fruitName;
    private String fruitImageUrl;
    private BigDecimal fruitPrice;
    private Integer quantity;
    private BigDecimal totalPrice;
    private LocalDate addedDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}