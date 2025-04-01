package com.fruitmall.domain.review.domain;

import com.fruitmall.domain.member.domain.Member;
import com.fruitmall.domain.order.domain.OrderDetail;
import com.fruitmall.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "review")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_detail_id")
    private OrderDetail orderDetail;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(nullable = false)
    private Integer rating;

    @Column(nullable = false, length = 1000)
    private String content;

    @Column(nullable = false)
    private LocalDate reviewDate;

    private String imageUrl;

    @Builder
    public Review(OrderDetail orderDetail, Member member, Integer rating, String content, String imageUrl) {
        this.orderDetail = orderDetail;
        this.member = member;
        this.rating = validateRating(rating);
        this.content = content;
        this.reviewDate = LocalDate.now();
        this.imageUrl = imageUrl;
    }

    // 평점 유효성 검증 (1-5점 사이)
    private Integer validateRating(Integer rating) {
        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("평점은 1점에서 5점 사이여야 합니다.");
        }
        return rating;
    }

    // 리뷰 내용 업데이트
    public void updateContent(String content) {
        this.content = content;
    }

    // 리뷰 평점 업데이트
    public void updateRating(Integer rating) {
        this.rating = validateRating(rating);
    }

    // 리뷰 이미지 업데이트
    public void updateImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    // 리뷰 업데이트
    public void update(Integer rating, String content, String imageUrl) {
        this.rating = validateRating(rating);
        this.content = content;
        this.imageUrl = imageUrl;
    }
}