package com.fruitmall.domain.cart.domain;

import com.fruitmall.domain.fruit.domain.Fruit;
import com.fruitmall.domain.member.domain.Member;
import com.fruitmall.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "cart", 
       uniqueConstraints = {
           @UniqueConstraint(columnNames = {"member_id", "fruit_id"})
       })
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Cart extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fruit_id")
    private Fruit fruit;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private LocalDate addedDate;

    @Builder
    public Cart(Member member, Fruit fruit, Integer quantity) {
        this.member = member;
        this.fruit = fruit;
        this.quantity = quantity;
        this.addedDate = LocalDate.now();
    }

    // 수량 변경
    public void updateQuantity(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("수량은 1 이상이어야 합니다.");
        }
        this.quantity = quantity;
    }

    // 수량 증가
    public void increaseQuantity(int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("증가량은 양수여야 합니다.");
        }
        this.quantity += amount;
    }

    // 수량 감소
    public void decreaseQuantity(int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("감소량은 양수여야 합니다.");
        }
        
        int newQuantity = this.quantity - amount;
        if (newQuantity <= 0) {
            throw new IllegalArgumentException("감소 후 수량은 1 이상이어야 합니다.");
        }
        
        this.quantity = newQuantity;
    }
}