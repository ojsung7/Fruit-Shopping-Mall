package com.fruitmall.domain.delivery.application.dto;

import com.fruitmall.domain.delivery.domain.DeliveryStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateDeliveryStatusRequestDto {
    
    @NotNull(message = "배송 상태는 필수 입력값입니다")
    private DeliveryStatus deliveryStatus;
}