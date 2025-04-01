package com.fruitmall.domain.order.infra.persistence;

import com.fruitmall.domain.fruit.domain.Fruit;
import com.fruitmall.domain.order.domain.Order;
import com.fruitmall.domain.order.domain.OrderDetail;
import com.fruitmall.domain.order.domain.OrderDetailRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JpaOrderDetailRepository extends JpaRepository<OrderDetail, Long>, OrderDetailRepository {
    
    List<OrderDetail> findByOrder(Order order);
    
    List<OrderDetail> findByFruit(Fruit fruit);
}