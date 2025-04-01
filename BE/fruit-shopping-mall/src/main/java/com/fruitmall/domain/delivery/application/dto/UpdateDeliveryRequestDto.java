package com.fruitmall.domain.delivery.application.dto;

import com.fruitmall.domain.delivery.domain.DeliveryStatus;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateDeliveryRequestDto {
    
    @NotNull(message = "배송 상태는 필수 입력값입니다")
    private DeliveryStatus deliveryStatus;
    
    @Future(message = "예상 배송일은 미래 날짜여야 합니다")
    private LocalDate expectedDeliveryDate;
    
    private String courier;
    
    private String trackingNumber;
}