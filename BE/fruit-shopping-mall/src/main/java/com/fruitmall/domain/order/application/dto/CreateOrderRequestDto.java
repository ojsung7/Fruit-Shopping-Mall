package com.fruitmall.domain.order.application.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderRequestDto {
    
    @NotEmpty(message = "주문 항목은 최소 1개 이상이어야 합니다")
    private List<@Valid OrderItemRequestDto> orderItems;
    
    @NotNull(message = "결제 방법은 필수 입력값입니다")
    private String paymentMethod;
}