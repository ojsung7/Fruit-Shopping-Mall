package com.fruitmall.domain.review.infra.persistence;

import com.fruitmall.domain.member.domain.Member;
import com.fruitmall.domain.order.domain.OrderDetail;
import com.fruitmall.domain.review.domain.Review;
import com.fruitmall.domain.review.domain.ReviewRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JpaReviewRepository extends JpaRepository<Review, Long>, ReviewRepository {
    
    List<Review> findByMember(Member member);
    
    Optional<Review> findByOrderDetail(OrderDetail orderDetail);
    
    boolean existsByOrderDetail(OrderDetail orderDetail);
    
    @Query("SELECT r FROM Review r JOIN r.orderDetail od JOIN od.fruit f WHERE f.id = :fruitId")
    List<Review> findByFruitId(@Param("fruitId") Long fruitId);
    
    @Query("SELECT r FROM Review r ORDER BY r.createdAt DESC")
    List<Review> findRecentReviews(Pageable pageable);
    
    default List<Review> findRecentReviews(int limit) {
        return findRecentReviews(PageRequest.of(0, limit));
    }
    
    @Query("SELECT r FROM Review r JOIN r.orderDetail od JOIN od.fruit f " +
           "WHERE f.id = :fruitId ORDER BY r.rating DESC")
    List<Review> findByFruitIdOrderByRatingDesc(@Param("fruitId") Long fruitId);
    
    @Query("SELECT AVG(r.rating) FROM Review r JOIN r.orderDetail od JOIN od.fruit f " +
           "WHERE f.id = :fruitId")
    Double calculateAverageRatingByFruitId(@Param("fruitId") Long fruitId);
    
    @Query("SELECT COUNT(r) FROM Review r JOIN r.orderDetail od JOIN od.fruit f " +
           "WHERE f.id = :fruitId")
    Long countByFruitId(@Param("fruitId") Long fruitId);
}