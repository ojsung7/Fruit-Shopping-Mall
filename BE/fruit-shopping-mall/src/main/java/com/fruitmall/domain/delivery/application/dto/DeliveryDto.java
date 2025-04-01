package com.fruitmall.domain.delivery.application.dto;

import com.fruitmall.domain.delivery.domain.DeliveryStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
public class DeliveryDto {
    
    private Long id;
    private Long orderId;
    private String orderNumber;
    private DeliveryStatus deliveryStatus;
    private LocalDate expectedDeliveryDate;
    private LocalDate actualDeliveryDate;
    private String courier;
    private String trackingNumber;
    private AddressDto address;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}