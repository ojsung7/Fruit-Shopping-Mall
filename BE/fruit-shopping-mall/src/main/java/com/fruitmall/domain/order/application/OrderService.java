package com.fruitmall.domain.order.application;

import com.fruitmall.domain.fruit.domain.Fruit;
import com.fruitmall.domain.fruit.domain.FruitRepository;
import com.fruitmall.domain.member.domain.Member;
import com.fruitmall.domain.member.domain.MemberRepository;
import com.fruitmall.domain.order.application.dto.*;
import com.fruitmall.domain.order.application.mapper.OrderMapper;
import com.fruitmall.domain.order.domain.Order;
import com.fruitmall.domain.order.domain.OrderDetail;
import com.fruitmall.domain.order.domain.OrderRepository;
import com.fruitmall.domain.order.domain.OrderStatus;
import com.fruitmall.global.error.BusinessException;
import com.fruitmall.global.error.ErrorCode;
import com.fruitmall.global.security.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {

    private final OrderRepository orderRepository;
    private final FruitRepository fruitRepository;
    private final MemberRepository memberRepository;
    private final OrderMapper orderMapper;

    // 주문 생성
    @Transactional
    public OrderDto createOrder(Long memberId, CreateOrderRequestDto dto) {
        // 회원 조회
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));
        
        // 새 주문 생성
        Order order = Order.builder()
                .member(member)
                .orderDate(LocalDate.now())
                .totalPrice(BigDecimal.ZERO)  // 임시 총액 (나중에 재계산)
                .paymentMethod(dto.getPaymentMethod())
                .build();
        
        // 주문 상세 항목 처리
        List<OrderDetail> orderDetails = new ArrayList<>();
        
        for (OrderItemRequestDto item : dto.getOrderItems()) {
            // 상품 조회
            Fruit fruit = fruitRepository.findById(item.getFruitId())
                    .orElseThrow(() -> new BusinessException(ErrorCode.FRUIT_NOT_FOUND));
            
            // 재고 확인
            if (fruit.getStockQuantity() < item.getQuantity()) {
                throw new BusinessException(ErrorCode.OUT_OF_STOCK);
            }
            
            // 재고 감소
            fruit.decreaseStock(item.getQuantity());
            
            // 주문 상세 생성
            OrderDetail orderDetail = OrderDetail.builder()
                    .fruit(fruit)
                    .quantity(item.getQuantity())
                    .unitPrice(fruit.getPrice())  // 현재 가격으로 저장
                    .build();
            
            orderDetails.add(orderDetail);
            order.addOrderDetail(orderDetail);
        }
        
        // 총 가격 계산
        order.recalculateTotalPrice();
        
        // 주문 저장
        Order savedOrder = orderRepository.save(order);
        
        return orderMapper.toDto(savedOrder);
    }

    // 주문 조회
    public OrderDto findById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.ORDER_NOT_FOUND));
        
        // 보안 검증: 본인 주문 또는 관리자만 조회 가능
        if (!isOwnerOrAdmin(order)) {
            throw new BusinessException(ErrorCode.ACCESS_DENIED);
        }
        
        return orderMapper.toDto(order);
    }

    // 회원의 모든 주문 조회
    public List<OrderDto> findByMember(Long memberId) {
        // 본인 주문 또는 관리자만 조회 가능
        String currentUsername = SecurityUtil.getCurrentUsername()
                .orElseThrow(() -> new BusinessException(ErrorCode.ACCESS_DENIED));
        
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));
        
        // 요청한 회원이 본인이 아니고 관리자도 아니면 접근 거부
        if (!currentUsername.equals(member.getUsername()) && !SecurityUtil.isAdmin()) {
            throw new BusinessException(ErrorCode.ACCESS_DENIED);
        }
        
        return orderRepository.findByMember(member).stream()
                .map(orderMapper::toDto)
                .collect(Collectors.toList());
    }

    // 주문 상태별 조회
    public List<OrderDto> findByOrderStatus(OrderStatus status) {
        // 관리자만 전체 주문 상태 조회 가능
        if (!SecurityUtil.isAdmin()) {
            throw new BusinessException(ErrorCode.ACCESS_DENIED);
        }
        
        return orderRepository.findByOrderStatus(status).stream()
                .map(orderMapper::toDto)
                .collect(Collectors.toList());
    }

    // 주문 기간별 조회
    public List<OrderDto> findByOrderDateBetween(LocalDate startDate, LocalDate endDate) {
        // 관리자만 전체 주문 기간 조회 가능
        if (!SecurityUtil.isAdmin()) {
            throw new BusinessException(ErrorCode.ACCESS_DENIED);
        }
        
        return orderRepository.findByOrderDateBetween(startDate, endDate).stream()
                .map(orderMapper::toDto)
                .collect(Collectors.toList());
    }

    // 주문 상태 업데이트
    @Transactional
    public OrderDto updateOrderStatus(Long id, UpdateOrderStatusRequestDto dto) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.ORDER_NOT_FOUND));
        
        // 일반 회원은 본인 주문에 대해 CANCELLED 상태로만 변경 가능
        if (!SecurityUtil.isAdmin()) {
            if (!isOwner(order) || dto.getOrderStatus() != OrderStatus.CANCELLED) {
                throw new BusinessException(ErrorCode.ACCESS_DENIED);
            }
            
            // 주문 취소 처리
            try {
                order.cancel();
                
                // 재고 복구
                order.getOrderDetails().forEach(orderDetail -> {
                    Fruit fruit = orderDetail.getFruit();
                    fruit.increaseStock(orderDetail.getQuantity());
                });
            } catch (IllegalStateException e) {
                throw new BusinessException(ErrorCode.CANNOT_CANCEL_ORDER);
            }
        } else {
            // 관리자는 모든 주문 상태 변경 가능
            order.updateOrderStatus(dto.getOrderStatus());
            
            // 취소로 변경하는 경우 재고 복구
            if (dto.getOrderStatus() == OrderStatus.CANCELLED) {
                order.getOrderDetails().forEach(orderDetail -> {
                    Fruit fruit = orderDetail.getFruit();
                    fruit.increaseStock(orderDetail.getQuantity());
                });
            }
        }
        
        return orderMapper.toDto(order);
    }

    // 주문 삭제 (관리자만 가능)
    @Transactional
    public void deleteOrder(Long id) {
        // 관리자만 주문 삭제 가능
        if (!SecurityUtil.isAdmin()) {
            throw new BusinessException(ErrorCode.ACCESS_DENIED);
        }
        
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.ORDER_NOT_FOUND));
        
        orderRepository.delete(order);
    }
    
    // 현재 사용자가 주문의 소유자인지 확인
    private boolean isOwner(Order order) {
        return SecurityUtil.getCurrentUsername()
                .map(username -> order.getMember() != null && username.equals(order.getMember().getUsername()))
                .orElse(false);
    }
    
    // 현재 사용자가 주문의 소유자나 관리자인지 확인
    private boolean isOwnerOrAdmin(Order order) {
        return isOwner(order) || SecurityUtil.isAdmin();
    }
}