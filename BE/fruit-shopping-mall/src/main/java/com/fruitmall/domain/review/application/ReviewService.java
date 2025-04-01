package com.fruitmall.domain.review.application;

import com.fruitmall.domain.fruit.domain.Fruit;
import com.fruitmall.domain.fruit.domain.FruitRepository;
import com.fruitmall.domain.member.domain.Member;
import com.fruitmall.domain.member.domain.MemberRepository;
import com.fruitmall.domain.order.domain.OrderDetail;
import com.fruitmall.domain.order.domain.OrderDetailRepository;
import com.fruitmall.domain.order.domain.OrderStatus;
import com.fruitmall.domain.review.application.dto.CreateReviewRequestDto;
import com.fruitmall.domain.review.application.dto.ReviewDto;
import com.fruitmall.domain.review.application.dto.ReviewSummaryDto;
import com.fruitmall.domain.review.application.dto.UpdateReviewRequestDto;
import com.fruitmall.domain.review.application.mapper.ReviewMapper;
import com.fruitmall.domain.review.domain.Review;
import com.fruitmall.domain.review.domain.ReviewRepository;
import com.fruitmall.global.error.BusinessException;
import com.fruitmall.global.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final MemberRepository memberRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final FruitRepository fruitRepository;
    private final ReviewMapper reviewMapper;

    // 리뷰 작성
    @Transactional
    public ReviewDto createReview(Long memberId, CreateReviewRequestDto dto) {
        // 회원 조회
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));
        
        // 주문 상세 조회
        OrderDetail orderDetail = orderDetailRepository.findById(dto.getOrderDetailId())
                .orElseThrow(() -> new BusinessException(ErrorCode.ENTITY_NOT_FOUND, "주문 상세를 찾을 수 없습니다."));
        
        // 본인의 주문인지 확인
        if (!orderDetail.getOrder().getMember().getId().equals(memberId)) {
            throw new BusinessException(ErrorCode.ACCESS_DENIED, "본인의 주문에 대해서만 리뷰를 작성할 수 있습니다.");
        }
        
        // 주문이 배송 완료 상태인지 확인
        if (orderDetail.getOrder().getOrderStatus() != OrderStatus.DELIVERED) {
            throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE, "배송 완료된 상품에 대해서만 리뷰를 작성할 수 있습니다.");
        }
        
        // 이미 리뷰를 작성했는지 확인
        if (reviewRepository.existsByOrderDetail(orderDetail)) {
            throw new BusinessException(ErrorCode.ALREADY_REVIEWED, "이미 리뷰를 작성한 상품입니다.");
        }
        
        // 리뷰 생성
        Review review = Review.builder()
                .orderDetail(orderDetail)
                .member(member)
                .rating(dto.getRating())
                .content(dto.getContent())
                .imageUrl(dto.getImageUrl())
                .build();
        
        // 리뷰 저장
        Review savedReview = reviewRepository.save(review);
        
        return reviewMapper.toDto(savedReview);
    }

    // 리뷰 조회
    public ReviewDto findById(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new BusinessException(ErrorCode.REVIEW_NOT_FOUND));
                
        return reviewMapper.toDto(review);
    }

    // 회원이 작성한 리뷰 목록 조회
    public List<ReviewDto> findByMember(Long memberId) {
        // 회원 조회
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));
        
        // 리뷰 목록 조회
        List<Review> reviews = reviewRepository.findByMember(member);
        
        return reviewMapper.toDtoList(reviews);
    }

    // 상품의 리뷰 목록 조회
    public ReviewSummaryDto findByFruitId(Long fruitId) {
        // 상품 조회
        Fruit fruit = fruitRepository.findById(fruitId)
                .orElseThrow(() -> new BusinessException(ErrorCode.FRUIT_NOT_FOUND));
        
        // 리뷰 목록 조회
        List<Review> reviews = reviewRepository.findByFruitId(fruitId);
        List<ReviewDto> reviewDtos = reviewMapper.toDtoList(reviews);
        
        // 평균 평점 계산
        Double averageRating = reviewRepository.calculateAverageRatingByFruitId(fruitId);
        
        // 리뷰 개수 계산
        Long reviewCount = reviewRepository.countByFruitId(fruitId);
        
        return reviewMapper.toSummaryDto(fruitId, fruit.getFruitName(), averageRating, reviewCount, reviewDtos);
    }

    // 상품의 리뷰 목록 조회 (높은 평점순)
    public ReviewSummaryDto findByFruitIdOrderByRatingDesc(Long fruitId) {
        // 상품 조회
        Fruit fruit = fruitRepository.findById(fruitId)
                .orElseThrow(() -> new BusinessException(ErrorCode.FRUIT_NOT_FOUND));
        
        // 리뷰 목록 조회 (높은 평점순)
        List<Review> reviews = reviewRepository.findByFruitIdOrderByRatingDesc(fruitId);
        List<ReviewDto> reviewDtos = reviewMapper.toDtoList(reviews);
        
        // 평균 평점 계산
        Double averageRating = reviewRepository.calculateAverageRatingByFruitId(fruitId);
        
        // 리뷰 개수 계산
        Long reviewCount = reviewRepository.countByFruitId(fruitId);
        
        return reviewMapper.toSummaryDto(fruitId, fruit.getFruitName(), averageRating, reviewCount, reviewDtos);
    }

    // 최근 리뷰 목록 조회
    public List<ReviewDto> findRecentReviews(int limit) {
        List<Review> reviews = reviewRepository.findRecentReviews(limit);
        return reviewMapper.toDtoList(reviews);
    }

    // 리뷰 수정
    @Transactional
    public ReviewDto updateReview(Long reviewId, Long memberId, UpdateReviewRequestDto dto) {
        // 리뷰 조회
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new BusinessException(ErrorCode.REVIEW_NOT_FOUND));
        
        // 본인의 리뷰인지 확인
        if (!review.getMember().getId().equals(memberId)) {
            throw new BusinessException(ErrorCode.ACCESS_DENIED, "본인의 리뷰만 수정할 수 있습니다.");
        }
        
        // 리뷰 업데이트
        review.update(dto.getRating(), dto.getContent(), dto.getImageUrl());
        
        return reviewMapper.toDto(review);
    }

    // 리뷰 삭제
    @Transactional
    public void deleteReview(Long reviewId, Long memberId) {
        // 리뷰 조회
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new BusinessException(ErrorCode.REVIEW_NOT_FOUND));
        
        // 본인의 리뷰인지 확인 (관리자도 삭제 가능)
        if (!review.getMember().getId().equals(memberId) && !isAdmin(memberId)) {
            throw new BusinessException(ErrorCode.ACCESS_DENIED, "본인의 리뷰만 삭제할 수 있습니다.");
        }
        
        // 리뷰 삭제
        reviewRepository.delete(review);
    }
    
    // 관리자 여부 확인 (간단한 구현, 실제로는 SecurityUtil 등을 사용)
    private boolean isAdmin(Long memberId) {
        // 구현 생략 (SecurityUtil.isAdmin() 등 활용)
        return false;
    }
}