package com.fruitmall.domain.review.domain;

import com.fruitmall.domain.fruit.domain.Fruit;
import com.fruitmall.domain.member.domain.Member;
import com.fruitmall.domain.order.domain.OrderDetail;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository {
    
    Review save(Review review);
    
    Optional<Review> findById(Long id);
    
    List<Review> findAll();
    
    List<Review> findByMember(Member member);
    
    Optional<Review> findByOrderDetail(OrderDetail orderDetail);
    
    List<Review> findByFruitId(Long fruitId);
    
    boolean existsByOrderDetail(OrderDetail orderDetail);
    
    void delete(Review review);
    
    // 최근 리뷰 조회
    List<Review> findRecentReviews(int limit);
    
    // 높은 평점 순으로 조회
    List<Review> findByFruitIdOrderByRatingDesc(Long fruitId);
    
    // 특정 상품의 평균 평점 계산
    Double calculateAverageRatingByFruitId(Long fruitId);
    
    // 특정 상품의 리뷰 수 계산
    Long countByFruitId(Long fruitId);
}