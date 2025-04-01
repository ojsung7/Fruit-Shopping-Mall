package com.fruitmall.domain.cart.application.dto;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Builder
public class CartSummaryDto {
    
    private int totalItems;
    private BigDecimal totalPrice;
    private List<CartDto> cartItems;
}