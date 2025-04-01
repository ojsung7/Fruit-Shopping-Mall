package com.fruitmall.domain.delivery.infra.persistence;

import com.fruitmall.domain.delivery.domain.Delivery;
import com.fruitmall.domain.delivery.domain.DeliveryRepository;
import com.fruitmall.domain.delivery.domain.DeliveryStatus;
import com.fruitmall.domain.order.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface JpaDeliveryRepository extends JpaRepository<Delivery, Long>, DeliveryRepository {
    
    Optional<Delivery> findByOrder(Order order);
    
    List<Delivery> findByDeliveryStatus(DeliveryStatus status);
    
    List<Delivery> findByExpectedDeliveryDate(LocalDate date);
    
    List<Delivery> findByExpectedDeliveryDateBetween(LocalDate fromDate, LocalDate toDate);
    
    List<Delivery> findByActualDeliveryDate(LocalDate date);
    
    List<Delivery> findByCourier(String courier);
    
    Optional<Delivery> findByTrackingNumber(String trackingNumber);
}