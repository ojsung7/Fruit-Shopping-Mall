package com.fruitmall.domain.fruit.application.dto;

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
public class StockUpdateDto {
    
    @NotNull(message = "재고 수량은 필수 입력값입니다")
    @Min(value = 0, message = "재고 수량은 0 이상이어야 합니다")
    private Integer stockQuantity;
}