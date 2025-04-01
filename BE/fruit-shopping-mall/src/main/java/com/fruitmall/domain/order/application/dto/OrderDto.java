package com.fruitmall.domain.order.application.dto;

import com.fruitmall.domain.order.domain.OrderStatus;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class OrderDto {
    
    private Long id;
    private Long memberId;
    private String memberName;
    private LocalDate orderDate;
    private BigDecimal totalPrice;
    private String paymentMethod;
    private OrderStatus orderStatus;
    private List<OrderDetailDto> orderDetails;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}