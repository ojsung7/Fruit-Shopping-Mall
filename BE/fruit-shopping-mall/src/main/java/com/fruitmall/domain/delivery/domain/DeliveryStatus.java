package com.fruitmall.domain.delivery.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DeliveryStatus {
    PREPARING("배송준비중"),
    SHIPPING("배송중"),
    DELIVERED("배송완료"),
    CANCELLED("배송취소");

    private final String displayValue;
}