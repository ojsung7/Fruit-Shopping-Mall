package com.fruitmall.domain.order.application.mapper;

import com.fruitmall.domain.order.application.dto.OrderDetailDto;
import com.fruitmall.domain.order.application.dto.OrderDto;
import com.fruitmall.domain.order.domain.Order;
import com.fruitmall.domain.order.domain.OrderDetail;

import java.util.List;

public interface OrderMapper {
    
    OrderDto toDto(Order order);
    
    OrderDetailDto toDetailDto(OrderDetail orderDetail);
    
    List<OrderDetailDto> toDetailDtoList(List<OrderDetail> orderDetails);
}