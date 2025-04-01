package com.fruitmall.domain.fruit.domain;

import com.fruitmall.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "fruit")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Fruit extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fruit_id")
    private Long id;

    @Column(nullable = false)
    private String fruitName;

    private String origin;

    @Column(nullable = false)
    private Integer stockQuantity;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    private String season;

    @Column(length = 1000)
    private String description;

    private String imageUrl;

    @Builder
    public Fruit(String fruitName, String origin, Integer stockQuantity, BigDecimal price, 
                 Category category, String season, String description, String imageUrl) {
        this.fruitName = fruitName;
        this.origin = origin;
        this.stockQuantity = stockQuantity;
        this.price = price;
        this.category = category;
        this.season = season;
        this.description = description;
        this.imageUrl = imageUrl;
    }

    // 재고 감소
    public void decreaseStock(int quantity) {
        int restStock = this.stockQuantity - quantity;
        if (restStock < 0) {
            throw new IllegalStateException("재고가 부족합니다.");
        }
        this.stockQuantity = restStock;
    }

    // 재고 증가
    public void increaseStock(int quantity) {
        this.stockQuantity += quantity;
    }

    // 상품 정보 업데이트
    public void updateInfo(String fruitName, String origin, BigDecimal price, 
                          Category category, String season, String description, String imageUrl) {
        this.fruitName = fruitName;
        this.origin = origin;
        this.price = price;
        this.category = category;
        this.season = season;
        this.description = description;
        this.imageUrl = imageUrl;
    }

    // 재고 업데이트
    public void updateStock(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }
}