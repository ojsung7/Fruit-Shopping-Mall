package com.fruitmall.domain.delivery.application;

import com.fruitmall.domain.delivery.application.dto.*;
import com.fruitmall.domain.delivery.application.mapper.DeliveryMapper;
import com.fruitmall.domain.delivery.domain.Address;
import com.fruitmall.domain.delivery.domain.Delivery;
import com.fruitmall.domain.delivery.domain.DeliveryRepository;
import com.fruitmall.domain.delivery.domain.DeliveryStatus;
import com.fruitmall.domain.order.domain.Order;
import com.fruitmall.domain.order.domain.OrderRepository;
import com.fruitmall.domain.order.domain.OrderStatus;
import com.fruitmall.global.error.BusinessException;
import com.fruitmall.global.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DeliveryService {

    private final DeliveryRepository deliveryRepository;
    private final OrderRepository orderRepository;
    private final DeliveryMapper deliveryMapper;

    // 배송 정보 생성
    @Transactional
    public DeliveryDto createDelivery(CreateDeliveryRequestDto dto) {
        // 주문 조회
        Order order = orderRepository.findById(dto.getOrderId())
                .orElseThrow(() -> new BusinessException(ErrorCode.ORDER_NOT_FOUND));
        
        // 이미 배송 정보가 있는지 확인
        if (deliveryRepository.findByOrder(order).isPresent()) {
            throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE, "이미 해당 주문에 대한 배송 정보가 존재합니다.");
        }
        
        // 주문 상태가 결제 완료인지 확인
        if (order.getOrderStatus() != OrderStatus.PAID) {
            throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE, "결제 완료된 주문에 대해서만 배송 정보를 생성할 수 있습니다.");
        }
        
        // 주소 생성
        Address address = deliveryMapper.toAddressEntity(dto.getAddress());
        
        // 배송 생성
        Delivery delivery = Delivery.builder()
                .order(order)
                .deliveryStatus(dto.getDeliveryStatus())
                .expectedDeliveryDate(dto.getExpectedDeliveryDate())
                .courier(dto.getCourier())
                .trackingNumber(dto.getTrackingNumber())
                .address(address)
                .build();
        
        // 주문 상태 업데이트 (배송 준비중)
        order.updateOrderStatus(OrderStatus.PREPARING);
        
        // 배송 저장
        Delivery savedDelivery = deliveryRepository.save(delivery);
        
        return deliveryMapper.toDto(savedDelivery);
    }

    // 배송 정보 조회
    public DeliveryDto findById(Long id) {
        Delivery delivery = deliveryRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.DELIVERY_NOT_FOUND));
                
        return deliveryMapper.toDto(delivery);
    }

    // 주문으로 배송 정보 조회
    public DeliveryDto findByOrderId(Long orderId) {
        // 주문 조회
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new BusinessException(ErrorCode.ORDER_NOT_FOUND));
        
        // 배송 정보 조회
        Delivery delivery = deliveryRepository.findByOrder(order)
                .orElseThrow(() -> new BusinessException(ErrorCode.DELIVERY_NOT_FOUND));
                
        return deliveryMapper.toDto(delivery);
    }

    // 모든 배송 정보 조회
    public List<DeliveryDto> findAll() {
        List<Delivery> deliveries = deliveryRepository.findAll();
        return deliveryMapper.toDtoList(deliveries);
    }

    // 배송 상태별 조회
    public List<DeliveryDto> findByStatus(DeliveryStatus status) {
        List<Delivery> deliveries = deliveryRepository.findByDeliveryStatus(status);
        return deliveryMapper.toDtoList(deliveries);
    }

    // 예상 배송일별 조회
    public List<DeliveryDto> findByExpectedDeliveryDate(LocalDate date) {
        List<Delivery> deliveries = deliveryRepository.findByExpectedDeliveryDate(date);
        return deliveryMapper.toDtoList(deliveries);
    }

    // 기간별 예상 배송 조회
    public List<DeliveryDto> findByExpectedDeliveryDateBetween(LocalDate fromDate, LocalDate toDate) {
        List<Delivery> deliveries = deliveryRepository.findByExpectedDeliveryDateBetween(fromDate, toDate);
        return deliveryMapper.toDtoList(deliveries);
    }

    // 운송장 번호로 조회
    public DeliveryDto findByTrackingNumber(String trackingNumber) {
        Delivery delivery = deliveryRepository.findByTrackingNumber(trackingNumber)
                .orElseThrow(() -> new BusinessException(ErrorCode.DELIVERY_NOT_FOUND));
                
        return deliveryMapper.toDto(delivery);
    }

    // 배송 상태 업데이트
    @Transactional
    public DeliveryDto updateDeliveryStatus(Long id, UpdateDeliveryStatusRequestDto dto) {
        // 배송 정보 조회
        Delivery delivery = deliveryRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.DELIVERY_NOT_FOUND));
        
        // 배송 상태 업데이트
        delivery.updateStatus(dto.getDeliveryStatus());
        
        // 주문 상태도 함께 업데이트
        Order order = delivery.getOrder();
        if (dto.getDeliveryStatus() == DeliveryStatus.SHIPPING) {
            order.updateOrderStatus(OrderStatus.SHIPPED);
        } else if (dto.getDeliveryStatus() == DeliveryStatus.DELIVERED) {
            order.updateOrderStatus(OrderStatus.DELIVERED);
        } else if (dto.getDeliveryStatus() == DeliveryStatus.CANCELLED) {
            order.updateOrderStatus(OrderStatus.CANCELLED);
        }
        
        return deliveryMapper.toDto(delivery);
    }

    // 배송 정보 전체 업데이트
    @Transactional
    public DeliveryDto updateDelivery(Long id, UpdateDeliveryRequestDto dto) {
        // 배송 정보 조회
        Delivery delivery = deliveryRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.DELIVERY_NOT_FOUND));
        
        // 배송 정보 업데이트
        delivery.updateDeliveryInfo(
            dto.getDeliveryStatus(),
            dto.getExpectedDeliveryDate(),
            dto.getCourier(),
            dto.getTrackingNumber()
        );
        
        // 주문 상태도 함께 업데이트
        Order order = delivery.getOrder();
        if (dto.getDeliveryStatus() == DeliveryStatus.SHIPPING) {
            order.updateOrderStatus(OrderStatus.SHIPPED);
        } else if (dto.getDeliveryStatus() == DeliveryStatus.DELIVERED) {
            order.updateOrderStatus(OrderStatus.DELIVERED);
        } else if (dto.getDeliveryStatus() == DeliveryStatus.CANCELLED) {
            order.updateOrderStatus(OrderStatus.CANCELLED);
        }
        
        return deliveryMapper.toDto(delivery);
    }

    // 운송장 정보 업데이트
    @Transactional
    public DeliveryDto updateTrackingInfo(Long id, String courier, String trackingNumber) {
        // 배송 정보 조회
        Delivery delivery = deliveryRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.DELIVERY_NOT_FOUND));
        
        // 운송장 정보 업데이트
        delivery.updateTrackingInfo(courier, trackingNumber);
        
        return deliveryMapper.toDto(delivery);
    }

    // 배송 취소
    @Transactional
    public DeliveryDto cancelDelivery(Long id) {
        // 배송 정보 조회
        Delivery delivery = deliveryRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.DELIVERY_NOT_FOUND));
        
        // 배송중이거나 배송완료인 경우 취소 불가
        if (delivery.getDeliveryStatus() == DeliveryStatus.SHIPPING || 
            delivery.getDeliveryStatus() == DeliveryStatus.DELIVERED) {
            throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE, "이미 배송중이거나 배송완료된 경우 취소할 수 없습니다.");
        }
        
        // 배송 상태 취소로 변경
        delivery.updateStatus(DeliveryStatus.CANCELLED);
        
        // 주문 상태도 취소로 변경
        Order order = delivery.getOrder();
        order.updateOrderStatus(OrderStatus.CANCELLED);
        
        return deliveryMapper.toDto(delivery);
    }
}