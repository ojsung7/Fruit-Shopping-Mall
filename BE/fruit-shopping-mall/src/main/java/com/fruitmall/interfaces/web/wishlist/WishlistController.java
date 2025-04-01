package com.fruitmall.interfaces.web.wishlist;

import com.fruitmall.domain.wishlist.application.WishlistService;
import com.fruitmall.domain.wishlist.application.dto.AddToWishlistRequestDto;
import com.fruitmall.domain.wishlist.application.dto.WishlistDto;
import com.fruitmall.domain.wishlist.application.dto.WishlistSummaryDto;
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

@RestController
@RequestMapping("/wishlist")
@RequiredArgsConstructor
@Tag(name = "위시리스트", description = "위시리스트 관련 API")
public class WishlistController {

    private final WishlistService wishlistService;

    @Operation(summary = "위시리스트에 상품 추가", description = "위시리스트에 상품을 추가합니다")
    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<WishlistDto> addToWishlist(@Valid @RequestBody AddToWishlistRequestDto dto) {
        Long memberId = SecurityUtil.getCurrentMemberId()
                .orElseThrow(() -> new IllegalStateException("로그인 유저 정보가 없습니다"));
        
        WishlistDto wishlistDto = wishlistService.addToWishlist(memberId, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(wishlistDto);
    }

    @Operation(summary = "위시리스트 조회", description = "현재 로그인한 회원의 위시리스트를 조회합니다")
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<WishlistSummaryDto> getWishlist() {
        Long memberId = SecurityUtil.getCurrentMemberId()
                .orElseThrow(() -> new IllegalStateException("로그인 유저 정보가 없습니다"));
        
        WishlistSummaryDto wishlistSummary = wishlistService.getWishlistByMember(memberId);
        return ResponseEntity.ok(wishlistSummary);
    }

    @Operation(summary = "위시리스트 항목 조회", description = "위시리스트 항목을 ID로 조회합니다")
    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<WishlistDto> getWishlistItem(
            @Parameter(description = "위시리스트 항목 ID", required = true)
            @PathVariable("id") Long wishlistId) {
        Long memberId = SecurityUtil.getCurrentMemberId()
                .orElseThrow(() -> new IllegalStateException("로그인 유저 정보가 없습니다"));
        
        WishlistDto wishlistDto = wishlistService.findById(wishlistId, memberId);
        return ResponseEntity.ok(wishlistDto);
    }

    @Operation(summary = "상품 위시리스트 포함 여부 확인", description = "특정 상품이 위시리스트에 포함되어 있는지 확인합니다")
    @GetMapping("/check/{fruitId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Boolean> isInWishlist(
            @Parameter(description = "상품 ID", required = true)
            @PathVariable Long fruitId) {
        Long memberId = SecurityUtil.getCurrentMemberId()
                .orElseThrow(() -> new IllegalStateException("로그인 유저 정보가 없습니다"));
        
        boolean isInWishlist = wishlistService.isInWishlist(memberId, fruitId);
        return ResponseEntity.ok(isInWishlist);
    }

    @Operation(summary = "위시리스트 항목 삭제", description = "위시리스트에서 특정 항목을 삭제합니다")
    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> removeWishlistItem(
            @Parameter(description = "위시리스트 항목 ID", required = true)
            @PathVariable("id") Long wishlistId) {
        Long memberId = SecurityUtil.getCurrentMemberId()
                .orElseThrow(() -> new IllegalStateException("로그인 유저 정보가 없습니다"));
        
        wishlistService.removeWishlistItem(wishlistId, memberId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "위시리스트 비우기", description = "위시리스트의 모든 항목을 삭제합니다")
    @DeleteMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> clearWishlist() {
        Long memberId = SecurityUtil.getCurrentMemberId()
                .orElseThrow(() -> new IllegalStateException("로그인 유저 정보가 없습니다"));
        
        wishlistService.clearWishlist(memberId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "상품 위시리스트에서 삭제", description = "특정 상품을 위시리스트에서 삭제합니다")
    @DeleteMapping("/fruit/{fruitId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> removeByFruit(
            @Parameter(description = "상품 ID", required = true)
            @PathVariable Long fruitId) {
        Long memberId = SecurityUtil.getCurrentMemberId()
                .orElseThrow(() -> new IllegalStateException("로그인 유저 정보가 없습니다"));
        
        wishlistService.removeByFruit(memberId, fruitId);
        return ResponseEntity.noContent().build();
    }
}