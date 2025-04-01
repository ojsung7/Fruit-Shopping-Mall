package com.fruitmall.domain.order.infra.persistence;

import com.fruitmall.domain.member.domain.Member;
import com.fruitmall.domain.order.domain.Order;
import com.fruitmall.domain.order.domain.OrderRepository;
import com.fruitmall.domain.order.domain.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface JpaOrderRepository extends JpaRepository<Order, Long>, OrderRepository {
    
    List<Order> findByMember(Member member);
    
    List<Order> findByOrderStatus(OrderStatus status);
    
    List<Order> findByOrderDateBetween(LocalDate startDate, LocalDate endDate);
    
    List<Order> findByMemberAndOrderStatus(Member member, OrderStatus status);
}