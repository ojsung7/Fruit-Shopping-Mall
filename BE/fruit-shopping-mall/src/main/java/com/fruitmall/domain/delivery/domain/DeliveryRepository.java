package com.fruitmall.domain.delivery.domain;

import com.fruitmall.domain.order.domain.Order;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DeliveryRepository {
    
    Delivery save(Delivery delivery);
    
    Optional<Delivery> findById(Long id);
    
    Optional<Delivery> findByOrder(Order order);
    
    List<Delivery> findAll();
    
    List<Delivery> findByDeliveryStatus(DeliveryStatus status);
    
    List<Delivery> findByExpectedDeliveryDate(LocalDate date);
    
    List<Delivery> findByExpectedDeliveryDateBetween(LocalDate fromDate, LocalDate toDate);
    
    List<Delivery> findByActualDeliveryDate(LocalDate date);
    
    List<Delivery> findByCourier(String courier);
    
    Optional<Delivery> findByTrackingNumber(String trackingNumber);
    
    void delete(Delivery delivery);
}