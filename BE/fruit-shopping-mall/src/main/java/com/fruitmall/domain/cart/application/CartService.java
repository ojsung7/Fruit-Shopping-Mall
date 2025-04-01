package com.fruitmall.domain.cart.application;

import com.fruitmall.domain.cart.application.dto.AddToCartRequestDto;
import com.fruitmall.domain.cart.application.dto.CartDto;
import com.fruitmall.domain.cart.application.dto.CartSummaryDto;
import com.fruitmall.domain.cart.application.dto.UpdateCartQuantityRequestDto;
import com.fruitmall.domain.cart.application.mapper.CartMapper;
import com.fruitmall.domain.cart.domain.Cart;
import com.fruitmall.domain.cart.domain.CartRepository;
import com.fruitmall.domain.fruit.domain.Fruit;
import com.fruitmall.domain.fruit.domain.FruitRepository;
import com.fruitmall.domain.member.domain.Member;
import com.fruitmall.domain.member.domain.MemberRepository;
import com.fruitmall.global.error.BusinessException;
import com.fruitmall.global.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CartService {

    private final CartRepository cartRepository;
    private final MemberRepository memberRepository;
    private final FruitRepository fruitRepository;
    private final CartMapper cartMapper;

    // 장바구니에 상품 추가
    @Transactional
    public CartDto addToCart(Long memberId, AddToCartRequestDto dto) {
        // 회원 조회
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));
        
        // 상품 조회
        Fruit fruit = fruitRepository.findById(dto.getFruitId())
                .orElseThrow(() -> new BusinessException(ErrorCode.FRUIT_NOT_FOUND));
        
        // 재고 확인
        if (fruit.getStockQuantity() < dto.getQuantity()) {
            throw new BusinessException(ErrorCode.OUT_OF_STOCK);
        }
        
        // 이미 장바구니에 있는 상품인지 확인
        Cart cart = cartRepository.findByMemberAndFruit(member, fruit)
                .orElse(null);
        
        if (cart != null) {
            // 이미 존재하면 수량 증가
            cart.increaseQuantity(dto.getQuantity());
        } else {
            // 새로운 장바구니 항목 생성
            cart = Cart.builder()
                    .member(member)
                    .fruit(fruit)
                    .quantity(dto.getQuantity())
                    .build();
        }
        
        // 장바구니 저장
        Cart savedCart = cartRepository.save(cart);
        
        return cartMapper.toDto(savedCart);
    }

    // 회원의 장바구니 목록 조회
    public CartSummaryDto getCartByMember(Long memberId) {
        // 회원 조회
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));
        
        // 장바구니 항목 조회
        List<Cart> carts = cartRepository.findByMember(member);
        
        // DTO 변환
        List<CartDto> cartDtos = cartMapper.toDtoList(carts);
        
        // 요약 정보 생성
        return cartMapper.toSummaryDto(cartDtos);
    }

    // 장바구니 항목 조회
    public CartDto findById(Long cartId, Long memberId) {
        // 장바구니 항목 조회
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new BusinessException(ErrorCode.ENTITY_NOT_FOUND, "장바구니 항목을 찾을 수 없습니다."));
        
        // 본인 장바구니만 조회 가능
        if (!cart.getMember().getId().equals(memberId)) {
            throw new BusinessException(ErrorCode.ACCESS_DENIED);
        }
        
        return cartMapper.toDto(cart);
    }

    // 장바구니 수량 업데이트
    @Transactional
    public CartDto updateQuantity(Long cartId, Long memberId, UpdateCartQuantityRequestDto dto) {
        // 장바구니 항목 조회
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new BusinessException(ErrorCode.ENTITY_NOT_FOUND, "장바구니 항목을 찾을 수 없습니다."));
        
        // 본인 장바구니만 수정 가능
        if (!cart.getMember().getId().equals(memberId)) {
            throw new BusinessException(ErrorCode.ACCESS_DENIED);
        }
        
        // 재고 확인
        Fruit fruit = cart.getFruit();
        if (fruit.getStockQuantity() < dto.getQuantity()) {
            throw new BusinessException(ErrorCode.OUT_OF_STOCK);
        }
        
        // 수량 업데이트
        cart.updateQuantity(dto.getQuantity());
        
        return cartMapper.toDto(cart);
    }

    // 장바구니 항목 삭제
    @Transactional
    public void removeCartItem(Long cartId, Long memberId) {
        // 장바구니 항목 조회
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new BusinessException(ErrorCode.ENTITY_NOT_FOUND, "장바구니 항목을 찾을 수 없습니다."));
        
        // 본인 장바구니만 삭제 가능
        if (!cart.getMember().getId().equals(memberId)) {
            throw new BusinessException(ErrorCode.ACCESS_DENIED);
        }
        
        // 항목 삭제
        cartRepository.delete(cart);
    }

    // 장바구니 비우기
    @Transactional
    public void clearCart(Long memberId) {
        // 회원 조회
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));
        
        // 장바구니 비우기
        cartRepository.deleteByMember(member);
    }

    // 상품 하나 장바구니에서 삭제
    @Transactional
    public void removeByFruit(Long memberId, Long fruitId) {
        // 회원 조회
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));
        
        // 상품 조회
        Fruit fruit = fruitRepository.findById(fruitId)
                .orElseThrow(() -> new BusinessException(ErrorCode.FRUIT_NOT_FOUND));
        
        // 장바구니에서 삭제
        cartRepository.deleteByMemberAndFruit(member, fruit);
    }
}