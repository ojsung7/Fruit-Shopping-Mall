package com.fruitmall.domain.order.domain;

import com.fruitmall.domain.fruit.domain.Fruit;
import com.fruitmall.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "order_detail")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderDetail extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_detail_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fruit_id")
    private Fruit fruit;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal unitPrice;

    @Builder
    public OrderDetail(Fruit fruit, Integer quantity, BigDecimal unitPrice) {
        this.fruit = fruit;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    // 주문 연관관계 설정 (양방향 연관관계 관리)
    public void setOrder(Order order) {
        this.order = order;
    }

    // 주문 상세 총액 계산
    public BigDecimal getTotalPrice() {
        return this.unitPrice.multiply(new BigDecimal(this.quantity));
    }
}