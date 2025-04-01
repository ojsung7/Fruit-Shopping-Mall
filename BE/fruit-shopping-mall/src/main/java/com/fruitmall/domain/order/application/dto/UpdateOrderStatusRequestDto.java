package com.fruitmall.domain.order.application.dto;

import com.fruitmall.domain.order.domain.OrderStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateOrderStatusRequestDto {
    
    @NotNull(message = "주문 상태는 필수 입력값입니다")
    private OrderStatus orderStatus;
}