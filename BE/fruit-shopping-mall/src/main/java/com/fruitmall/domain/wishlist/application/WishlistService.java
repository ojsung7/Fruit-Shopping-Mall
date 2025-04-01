package com.fruitmall.domain.wishlist.application;

import com.fruitmall.domain.fruit.domain.Fruit;
import com.fruitmall.domain.fruit.domain.FruitRepository;
import com.fruitmall.domain.member.domain.Member;
import com.fruitmall.domain.member.domain.MemberRepository;
import com.fruitmall.domain.wishlist.application.dto.AddToWishlistRequestDto;
import com.fruitmall.domain.wishlist.application.dto.WishlistDto;
import com.fruitmall.domain.wishlist.application.dto.WishlistSummaryDto;
import com.fruitmall.domain.wishlist.application.mapper.WishlistMapper;
import com.fruitmall.domain.wishlist.domain.Wishlist;
import com.fruitmall.domain.wishlist.domain.WishlistRepository;
import com.fruitmall.global.error.BusinessException;
import com.fruitmall.global.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WishlistService {

    private final WishlistRepository wishlistRepository;
    private final MemberRepository memberRepository;
    private final FruitRepository fruitRepository;
    private final WishlistMapper wishlistMapper;

    // 위시리스트에 상품 추가
    @Transactional
    public WishlistDto addToWishlist(Long memberId, AddToWishlistRequestDto dto) {
        // 회원 조회
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));
        
        // 상품 조회
        Fruit fruit = fruitRepository.findById(dto.getFruitId())
                .orElseThrow(() -> new BusinessException(ErrorCode.FRUIT_NOT_FOUND));
        
        // 이미 위시리스트에 있는지 확인
        if (wishlistRepository.existsByMemberAndFruit(member, fruit)) {
            throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE, "이미 위시리스트에 추가된 상품입니다.");
        }
        
        // 새로운 위시리스트 항목 생성
        Wishlist wishlist = Wishlist.builder()
                .member(member)
                .fruit(fruit)
                .build();
        
        // 위시리스트 저장
        Wishlist savedWishlist = wishlistRepository.save(wishlist);
        
        return wishlistMapper.toDto(savedWishlist);
    }

    // 회원의 위시리스트 목록 조회
    public WishlistSummaryDto getWishlistByMember(Long memberId) {
        // 회원 조회
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));
        
        // 위시리스트 항목 조회
        List<Wishlist> wishlists = wishlistRepository.findByMember(member);
        
        // DTO 변환
        List<WishlistDto> wishlistDtos = wishlistMapper.toDtoList(wishlists);
        
        // 요약 정보 생성
        return wishlistMapper.toSummaryDto(wishlistDtos);
    }

    // 위시리스트 항목 조회
    public WishlistDto findById(Long wishlistId, Long memberId) {
        // 위시리스트 항목 조회
        Wishlist wishlist = wishlistRepository.findById(wishlistId)
                .orElseThrow(() -> new BusinessException(ErrorCode.ENTITY_NOT_FOUND, "위시리스트 항목을 찾을 수 없습니다."));
        
        // 본인 위시리스트만 조회 가능
        if (!wishlist.getMember().getId().equals(memberId)) {
            throw new BusinessException(ErrorCode.ACCESS_DENIED);
        }
        
        return wishlistMapper.toDto(wishlist);
    }

    // 위시리스트 상품 포함 여부 확인
    public boolean isInWishlist(Long memberId, Long fruitId) {
        // 회원 조회
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));
        
        // 상품 조회
        Fruit fruit = fruitRepository.findById(fruitId)
                .orElseThrow(() -> new BusinessException(ErrorCode.FRUIT_NOT_FOUND));
        
        // 위시리스트에 포함되어 있는지 확인
        return wishlistRepository.existsByMemberAndFruit(member, fruit);
    }

    // 위시리스트 항목 삭제
    @Transactional
    public void removeWishlistItem(Long wishlistId, Long memberId) {
        // 위시리스트 항목 조회
        Wishlist wishlist = wishlistRepository.findById(wishlistId)
                .orElseThrow(() -> new BusinessException(ErrorCode.ENTITY_NOT_FOUND, "위시리스트 항목을 찾을 수 없습니다."));
        
        // 본인 위시리스트만 삭제 가능
        if (!wishlist.getMember().getId().equals(memberId)) {
            throw new BusinessException(ErrorCode.ACCESS_DENIED);
        }
        
        // 항목 삭제
        wishlistRepository.delete(wishlist);
    }

    // 위시리스트 비우기
    @Transactional
    public void clearWishlist(Long memberId) {
        // 회원 조회
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));
        
        // 위시리스트 비우기
        wishlistRepository.deleteByMember(member);
    }

    // 상품 하나 위시리스트에서 삭제
    @Transactional
    public void removeByFruit(Long memberId, Long fruitId) {
        // 회원 조회
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));
        
        // 상품 조회
        Fruit fruit = fruitRepository.findById(fruitId)
                .orElseThrow(() -> new BusinessException(ErrorCode.FRUIT_NOT_FOUND));
        
        // 위시리스트에서 삭제
        wishlistRepository.deleteByMemberAndFruit(member, fruit);
    }
}