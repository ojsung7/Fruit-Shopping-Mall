package com.fruitmall.domain.order.domain;

import com.fruitmall.domain.fruit.domain.Fruit;

import java.util.List;
import java.util.Optional;

public interface OrderDetailRepository {
    
    OrderDetail save(OrderDetail orderDetail);
    
    Optional<OrderDetail> findById(Long id);
    
    List<OrderDetail> findByOrder(Order order);
    
    List<OrderDetail> findByFruit(Fruit fruit);
    
    void delete(OrderDetail orderDetail);
}