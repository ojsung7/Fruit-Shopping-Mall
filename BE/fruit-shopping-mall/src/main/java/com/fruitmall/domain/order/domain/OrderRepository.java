package com.fruitmall.domain.order.domain;

import com.fruitmall.domain.member.domain.Member;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface OrderRepository {
    
    Order save(Order order);
    
    Optional<Order> findById(Long id);
    
    List<Order> findAll();
    
    List<Order> findByMember(Member member);
    
    List<Order> findByOrderStatus(OrderStatus status);
    
    List<Order> findByOrderDateBetween(LocalDate startDate, LocalDate endDate);
    
    List<Order> findByMemberAndOrderStatus(Member member, OrderStatus status);
    
    void delete(Order order);
}