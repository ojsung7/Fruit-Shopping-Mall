package com.fruitmall.domain.wishlist.application.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddToWishlistRequestDto {
    
    @NotNull(message = "상품 ID는 필수 입력값입니다")
    private Long fruitId;
}