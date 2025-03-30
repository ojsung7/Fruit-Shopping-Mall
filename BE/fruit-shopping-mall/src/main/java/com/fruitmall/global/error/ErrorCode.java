package com.fruitmall.global.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // 공통 에러 코드
    INVALID_INPUT_VALUE(400, "COMMON-001", "유효하지 않은 입력값입니다"),
    METHOD_NOT_ALLOWED(405, "COMMON-002", "허용되지 않은 메서드입니다"),
    ENTITY_NOT_FOUND(404, "COMMON-003", "엔티티를 찾을 수 없습니다"),
    INTERNAL_SERVER_ERROR(500, "COMMON-004", "서버 오류가 발생했습니다"),
    INVALID_TYPE_VALUE(400, "COMMON-005", "유효하지 않은 타입입니다"),
    ACCESS_DENIED(403, "COMMON-006", "접근이 거부되었습니다"),
    
    // 회원 관련 에러 코드
    MEMBER_NOT_FOUND(404, "MEMBER-001", "회원을 찾을 수 없습니다"),
    EMAIL_DUPLICATION(400, "MEMBER-002", "이미 사용 중인 이메일입니다"),
    USERNAME_DUPLICATION(400, "MEMBER-003", "이미 사용 중인 사용자 이름입니다"),
    INVALID_PASSWORD(400, "MEMBER-004", "잘못된 비밀번호입니다"),
    
    // 상품 관련 에러 코드
    FRUIT_NOT_FOUND(404, "FRUIT-001", "상품을 찾을 수 없습니다"),
    OUT_OF_STOCK(400, "FRUIT-002", "재고가 부족합니다"),
    
    // 주문 관련 에러 코드
    ORDER_NOT_FOUND(404, "ORDER-001", "주문을 찾을 수 없습니다"),
    CANNOT_CANCEL_ORDER(400, "ORDER-002", "주문을 취소할 수 없습니다"),
    
    // 장바구니 관련 에러 코드
    CART_NOT_FOUND(404, "CART-001", "장바구니를 찾을 수 없습니다"),
    
    // 결제 관련 에러 코드
    PAYMENT_FAILED(400, "PAYMENT-001", "결제에 실패했습니다"),
    
    // 배송 관련 에러 코드
    DELIVERY_NOT_FOUND(404, "DELIVERY-001", "배송 정보를 찾을 수 없습니다"),
    
    // 리뷰 관련 에러 코드
    REVIEW_NOT_FOUND(404, "REVIEW-001", "리뷰를 찾을 수 없습니다"),
    ALREADY_REVIEWED(400, "REVIEW-002", "이미 리뷰를 작성했습니다");

    private final int status;
    private final String code;
    private final String message;
}