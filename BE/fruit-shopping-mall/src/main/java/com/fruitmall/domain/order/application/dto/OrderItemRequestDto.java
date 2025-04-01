package com.fruitmall.domain.order.application.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemRequestDto {
    
    @NotNull(message = "상품 ID는 필수 입력값입니다")
    private Long fruitId;
    
    @NotNull(message = "수량은 필수 입력값입니다")
    @Min(value = 1, message = "수량은 최소 1개 이상이어야 합니다")
    private Integer quantity;
}