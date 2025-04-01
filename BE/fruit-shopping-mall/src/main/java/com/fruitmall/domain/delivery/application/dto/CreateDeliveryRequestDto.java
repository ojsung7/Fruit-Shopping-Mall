package com.fruitmall.domain.delivery.application.dto;

import com.fruitmall.domain.delivery.domain.DeliveryStatus;
import jakarta.validation.Valid;
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
public class CreateDeliveryRequestDto {
    
    @NotNull(message = "주문 ID는 필수 입력값입니다")
    private Long orderId;
    
    @NotNull(message = "배송 상태는 필수 입력값입니다")
    private DeliveryStatus deliveryStatus;
    
    @NotNull(message = "예상 배송일은 필수 입력값입니다")
    @Future(message = "예상 배송일은 미래 날짜여야 합니다")
    private LocalDate expectedDeliveryDate;
    
    private String courier;
    
    private String trackingNumber;
    
    @NotNull(message = "배송지 정보는 필수 입력값입니다")
    @Valid
    private AddressRequestDto address;
}