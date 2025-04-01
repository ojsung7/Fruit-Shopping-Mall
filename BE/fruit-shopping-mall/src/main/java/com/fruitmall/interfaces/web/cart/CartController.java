package com.fruitmall.interfaces.web.cart;

import com.fruitmall.domain.cart.application.CartService;
import com.fruitmall.domain.cart.application.dto.AddToCartRequestDto;
import com.fruitmall.domain.cart.application.dto.CartDto;
import com.fruitmall.domain.cart.application.dto.CartSummaryDto;
import com.fruitmall.domain.cart.application.dto.UpdateCartQuantityRequestDto;
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
@RequestMapping("/cart")
@RequiredArgsConstructor
@Tag(name = "장바구니", description = "장바구니 관련 API")
public class CartController {

    private final CartService cartService;

    @Operation(summary = "장바구니에 상품 추가", description = "장바구니에 상품을 추가합니다")
    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CartDto> addToCart(@Valid @RequestBody AddToCartRequestDto dto) {
        Long memberId = SecurityUtil.getCurrentMemberId()
                .orElseThrow(() -> new IllegalStateException("로그인 유저 정보가 없습니다"));
        
        CartDto cartDto = cartService.addToCart(memberId, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(cartDto);
    }

    @Operation(summary = "장바구니 조회", description = "현재 로그인한 회원의 장바구니를 조회합니다")
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CartSummaryDto> getCart() {
        Long memberId = SecurityUtil.getCurrentMemberId()
                .orElseThrow(() -> new IllegalStateException("로그인 유저 정보가 없습니다"));
        
        CartSummaryDto cartSummary = cartService.getCartByMember(memberId);
        return ResponseEntity.ok(cartSummary);
    }

    @Operation(summary = "장바구니 항목 조회", description = "장바구니 항목을 ID로 조회합니다")
    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CartDto> getCartItem(
            @Parameter(description = "장바구니 항목 ID", required = true)
            @PathVariable("id") Long cartId) {
        Long memberId = SecurityUtil.getCurrentMemberId()
                .orElseThrow(() -> new IllegalStateException("로그인 유저 정보가 없습니다"));
        
        CartDto cartDto = cartService.findById(cartId, memberId);
        return ResponseEntity.ok(cartDto);
    }

    @Operation(summary = "장바구니 수량 변경", description = "장바구니 항목의 수량을 변경합니다")
    @PutMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CartDto> updateCartQuantity(
            @Parameter(description = "장바구니 항목 ID", required = true)
            @PathVariable("id") Long cartId,
            @Valid @RequestBody UpdateCartQuantityRequestDto dto) {
        Long memberId = SecurityUtil.getCurrentMemberId()
                .orElseThrow(() -> new IllegalStateException("로그인 유저 정보가 없습니다"));
        
        CartDto updatedCart = cartService.updateQuantity(cartId, memberId, dto);
        return ResponseEntity.ok(updatedCart);
    }

    @Operation(summary = "장바구니 항목 삭제", description = "장바구니에서 특정 항목을 삭제합니다")
    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> removeCartItem(
            @Parameter(description = "장바구니 항목 ID", required = true)
            @PathVariable("id") Long cartId) {
        Long memberId = SecurityUtil.getCurrentMemberId()
                .orElseThrow(() -> new IllegalStateException("로그인 유저 정보가 없습니다"));
        
        cartService.removeCartItem(cartId, memberId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "장바구니 비우기", description = "장바구니의 모든 항목을 삭제합니다")
    @DeleteMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> clearCart() {
        Long memberId = SecurityUtil.getCurrentMemberId()
                .orElseThrow(() -> new IllegalStateException("로그인 유저 정보가 없습니다"));
        
        cartService.clearCart(memberId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "상품 장바구니에서 삭제", description = "특정 상품을 장바구니에서 삭제합니다")
    @DeleteMapping("/fruit/{fruitId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> removeByFruit(
            @Parameter(description = "상품 ID", required = true)
            @PathVariable Long fruitId) {
        Long memberId = SecurityUtil.getCurrentMemberId()
                .orElseThrow(() -> new IllegalStateException("로그인 유저 정보가 없습니다"));
        
        cartService.removeByFruit(memberId, fruitId);
        return ResponseEntity.noContent().build();
    }
}