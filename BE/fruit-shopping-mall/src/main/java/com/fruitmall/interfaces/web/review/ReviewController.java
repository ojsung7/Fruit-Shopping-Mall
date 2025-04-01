package com.fruitmall.interfaces.web.review;

import com.fruitmall.domain.review.application.ReviewService;
import com.fruitmall.domain.review.application.dto.CreateReviewRequestDto;
import com.fruitmall.domain.review.application.dto.ReviewDto;
import com.fruitmall.domain.review.application.dto.ReviewSummaryDto;
import com.fruitmall.domain.review.application.dto.UpdateReviewRequestDto;
import com.fruitmall.global.security.SecurityUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
@Tag(name = "리뷰", description = "상품 리뷰 관련 API")
public class ReviewController {

    private final ReviewService reviewService;

    @Operation(summary = "리뷰 작성", description = "상품에 대한 리뷰를 작성합니다")
    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ReviewDto> createReview(@Valid @RequestBody CreateReviewRequestDto dto) {
        Long memberId = SecurityUtil.getCurrentMemberId()
                .orElseThrow(() -> new IllegalStateException("로그인 유저 정보가 없습니다"));
        
        ReviewDto reviewDto = reviewService.createReview(memberId, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(reviewDto);
    }

    @Operation(summary = "리뷰 조회", description = "리뷰 ID로 리뷰를 조회합니다")
    @GetMapping("/{id}")
    public ResponseEntity<ReviewDto> getReview(
            @Parameter(description = "리뷰 ID", required = true)
            @PathVariable("id") Long reviewId) {
        ReviewDto reviewDto = reviewService.findById(reviewId);
        return ResponseEntity.ok(reviewDto);
    }

    @Operation(summary = "내 리뷰 목록 조회", description = "현재 로그인한 회원의 리뷰 목록을 조회합니다")
    @GetMapping("/my-reviews")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<ReviewDto>> getMyReviews() {
        Long memberId = SecurityUtil.getCurrentMemberId()
                .orElseThrow(() -> new IllegalStateException("로그인 유저 정보가 없습니다"));
        
        List<ReviewDto> reviews = reviewService.findByMember(memberId);
        return ResponseEntity.ok(reviews);
    }

    @Operation(summary = "상품 리뷰 목록 조회", description = "특정 상품의 리뷰 목록을 조회합니다")
    @GetMapping("/fruit/{fruitId}")
    public ResponseEntity<ReviewSummaryDto> getReviewsByFruit(
            @Parameter(description = "상품 ID", required = true)
            @PathVariable Long fruitId) {
        ReviewSummaryDto reviewSummary = reviewService.findByFruitId(fruitId);
        return ResponseEntity.ok(reviewSummary);
    }

    @Operation(summary = "상품 리뷰 목록 조회 (높은 평점순)", description = "특정 상품의 리뷰 목록을 높은 평점순으로 조회합니다")
    @GetMapping("/fruit/{fruitId}/high-rating")
    public ResponseEntity<ReviewSummaryDto> getReviewsByFruitOrderByRatingDesc(
            @Parameter(description = "상품 ID", required = true)
            @PathVariable Long fruitId) {
        ReviewSummaryDto reviewSummary = reviewService.findByFruitIdOrderByRatingDesc(fruitId);
        return ResponseEntity.ok(reviewSummary);
    }

    @Operation(summary = "최근 리뷰 목록 조회", description = "최근 작성된 리뷰 목록을 조회합니다")
    @GetMapping("/recent")
    public ResponseEntity<List<ReviewDto>> getRecentReviews(
            @Parameter(description = "조회할 리뷰 수", required = false)
            @RequestParam(defaultValue = "10") int limit) {
        List<ReviewDto> reviews = reviewService.findRecentReviews(limit);
        return ResponseEntity.ok(reviews);
    }

    @Operation(summary = "리뷰 수정", description = "작성한 리뷰를 수정합니다")
    @PutMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ReviewDto> updateReview(
            @Parameter(description = "리뷰 ID", required = true)
            @PathVariable("id") Long reviewId,
            @Valid @RequestBody UpdateReviewRequestDto dto) {
        Long memberId = SecurityUtil.getCurrentMemberId()
                .orElseThrow(() -> new IllegalStateException("로그인 유저 정보가 없습니다"));
        
        ReviewDto updatedReview = reviewService.updateReview(reviewId, memberId, dto);
        return ResponseEntity.ok(updatedReview);
    }

    @Operation(summary = "리뷰 삭제", description = "작성한 리뷰를 삭제합니다")
    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> deleteReview(
            @Parameter(description = "리뷰 ID", required = true)
            @PathVariable("id") Long reviewId) {
        Long memberId = SecurityUtil.getCurrentMemberId()
                .orElseThrow(() -> new IllegalStateException("로그인 유저 정보가 없습니다"));
        
        reviewService.deleteReview(reviewId, memberId);
        return ResponseEntity.noContent().build();
    }
}