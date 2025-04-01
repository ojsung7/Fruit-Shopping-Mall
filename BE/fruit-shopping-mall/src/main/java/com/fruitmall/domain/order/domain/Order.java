package com.fruitmall.domain.order.domain;

import com.fruitmall.domain.member.domain.Member;
import com.fruitmall.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")  // "order"는 SQL 예약어라 "orders"로 사용
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(nullable = false)
    private LocalDate orderDate;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal totalPrice;

    private String paymentMethod;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus orderStatus;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderDetail> orderDetails = new ArrayList<>();

    @Builder
    public Order(Member member, LocalDate orderDate, BigDecimal totalPrice, String paymentMethod) {
        this.member = member;
        this.orderDate = orderDate;
        this.totalPrice = totalPrice;
        this.paymentMethod = paymentMethod;
        this.orderStatus = OrderStatus.PENDING;  // 초기 상태는 '대기 중'
    }

    // 주문 상태 업데이트
    public void updateOrderStatus(OrderStatus status) {
        this.orderStatus = status;
    }

    // 주문 취소 (PENDING 또는 PAID 상태일 때만 가능)
    public void cancel() {
        if (this.orderStatus == OrderStatus.SHIPPED || this.orderStatus == OrderStatus.DELIVERED) {
            throw new IllegalStateException("이미 배송된 주문은 취소할 수 없습니다.");
        }
        this.orderStatus = OrderStatus.CANCELLED;
    }

    // 주문 상세 추가
    public void addOrderDetail(OrderDetail orderDetail) {
        this.orderDetails.add(orderDetail);
        orderDetail.setOrder(this);
    }

    // 총 가격 재계산
    public void recalculateTotalPrice() {
        this.totalPrice = this.orderDetails.stream()
                .map(od -> od.getUnitPrice().multiply(new BigDecimal(od.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}