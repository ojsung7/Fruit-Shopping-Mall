package com.fruitmall.domain.member.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MemberGrade {
    BRONZE("브론즈", 0, 0.0),
    SILVER("실버", 100000, 2.0),
    GOLD("골드", 300000, 5.0),
    PLATINUM("플래티넘", 1000000, 10.0),
    VIP("VIP", 3000000, 15.0);

    private final String displayName;
    private final int requiredAmount; // 등급 달성을 위한 누적 구매 금액
    private final double discountRate; // 할인율 (%)
}