package com.fruitmall.domain.delivery.domain;

import com.fruitmall.domain.order.domain.Order;
import com.fruitmall.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "delivery")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Delivery extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "delivery_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DeliveryStatus deliveryStatus;

    @Column(nullable = false)
    private LocalDate expectedDeliveryDate;

    private LocalDate actualDeliveryDate;

    private String courier;

    private String trackingNumber;

    @Embedded
    private Address address;

    @Builder
    public Delivery(Order order, DeliveryStatus deliveryStatus, LocalDate expectedDeliveryDate, 
                    String courier, String trackingNumber, Address address) {
        this.order = order;
        this.deliveryStatus = deliveryStatus;
        this.expectedDeliveryDate = expectedDeliveryDate;
        this.courier = courier;
        this.trackingNumber = trackingNumber;
        this.address = address;
    }

    // 배송 상태 업데이트
    public void updateStatus(DeliveryStatus status) {
        // 이미 배송 완료인 경우, 상태 변경 불가
        if (this.deliveryStatus == DeliveryStatus.DELIVERED && status != DeliveryStatus.DELIVERED) {
            throw new IllegalStateException("이미 배송 완료된 주문의 상태는 변경할 수 없습니다.");
        }
        
        // 배송중 -> 배송완료로 변경시 실제 배송일 설정
        if (status == DeliveryStatus.DELIVERED && this.deliveryStatus != DeliveryStatus.DELIVERED) {
            this.actualDeliveryDate = LocalDate.now();
        }
        
        this.deliveryStatus = status;
    }

    // 운송장 정보 업데이트
    public void updateTrackingInfo(String courier, String trackingNumber) {
        this.courier = courier;
        this.trackingNumber = trackingNumber;
    }

    // 예상 배송일 업데이트
    public void updateExpectedDeliveryDate(LocalDate expectedDeliveryDate) {
        this.expectedDeliveryDate = expectedDeliveryDate;
    }

    // 배송 정보 업데이트
    public void updateDeliveryInfo(DeliveryStatus status, LocalDate expectedDeliveryDate, 
                                  String courier, String trackingNumber) {
        updateStatus(status);
        this.expectedDeliveryDate = expectedDeliveryDate;
        this.courier = courier;
        this.trackingNumber = trackingNumber;
    }
}