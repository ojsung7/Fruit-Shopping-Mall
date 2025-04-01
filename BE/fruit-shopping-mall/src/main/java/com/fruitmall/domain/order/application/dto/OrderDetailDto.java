package com.fruitmall.domain.order.application.dto;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class OrderDetailDto {
    
    private Long id;
    private Long orderId;
    private Long fruitId;
    private String fruitName;
    private String fruitImageUrl;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal totalPrice;
}