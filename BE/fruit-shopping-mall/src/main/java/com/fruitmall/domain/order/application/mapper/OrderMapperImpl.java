package com.fruitmall.domain.order.application.mapper;

import com.fruitmall.domain.order.application.dto.OrderDetailDto;
import com.fruitmall.domain.order.application.dto.OrderDto;
import com.fruitmall.domain.order.domain.Order;
import com.fruitmall.domain.order.domain.OrderDetail;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderMapperImpl implements OrderMapper {

    @Override
    public OrderDto toDto(Order order) {
        if (order == null) {
            return null;
        }
        
        return OrderDto.builder()
                .id(order.getId())
                .memberId(order.getMember() != null ? order.getMember().getId() : null)
                .memberName(order.getMember() != null ? order.getMember().getName() : null)
                .orderDate(order.getOrderDate())
                .totalPrice(order.getTotalPrice())
                .paymentMethod(order.getPaymentMethod())
                .orderStatus(order.getOrderStatus())
                .orderDetails(toDetailDtoList(order.getOrderDetails()))
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .build();
    }

    @Override
    public OrderDetailDto toDetailDto(OrderDetail orderDetail) {
        if (orderDetail == null) {
            return null;
        }
        
        return OrderDetailDto.builder()
                .id(orderDetail.getId())
                .orderId(orderDetail.getOrder() != null ? orderDetail.getOrder().getId() : null)
                .fruitId(orderDetail.getFruit() != null ? orderDetail.getFruit().getId() : null)
                .fruitName(orderDetail.getFruit() != null ? orderDetail.getFruit().getFruitName() : null)
                .fruitImageUrl(orderDetail.getFruit() != null ? orderDetail.getFruit().getImageUrl() : null)
                .quantity(orderDetail.getQuantity())
                .unitPrice(orderDetail.getUnitPrice())
                .totalPrice(orderDetail.getTotalPrice())
                .build();
    }

    @Override
    public List<OrderDetailDto> toDetailDtoList(List<OrderDetail> orderDetails) {
        if (orderDetails == null) {
            return null;
        }
        
        return orderDetails.stream()
                .map(this::toDetailDto)
                .collect(Collectors.toList());
    }
}