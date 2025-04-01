package com.fruitmall.domain.cart.application.dto;

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
public class UpdateCartQuantityRequestDto {
    
    @NotNull(message = "수량은 필수 입력값입니다")
    @Min(value = 1, message = "수량은 최소 1개 이상이어야 합니다")
    private Integer quantity;
}